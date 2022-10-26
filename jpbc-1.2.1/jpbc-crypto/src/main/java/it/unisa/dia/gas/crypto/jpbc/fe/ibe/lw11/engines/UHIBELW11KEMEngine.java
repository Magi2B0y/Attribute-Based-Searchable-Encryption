package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.engines;

import it.unisa.dia.gas.crypto.engines.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11KEMEngine extends PairingKeyEncapsulationMechanism {

    private int length;

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof UHIBELW11EncryptionParameters)) {
                throw new IllegalArgumentException("UHIBELW11EncryptionParameters are required for encryption.");
            }

            this.pairing = PairingFactory.getPairing(((UHIBELW11EncryptionParameters) key).getPublicKey().getCurveParameters());
            this.length = ((UHIBELW11EncryptionParameters) key).getLength();
        } else {
            if (!(key instanceof UHIBELW11SecretKeyParameters)) {
                throw new IllegalArgumentException("UHIBELW11SecretKeyParameters are required for decryption.");
            }

            this.pairing = PairingFactory.getPairing(((UHIBELW11SecretKeyParameters) key).getCurveParameters());
            this.length = ((UHIBELW11SecretKeyParameters) key).getLength();
        }

        this.keyBytes = pairing.getGT().getLengthInBytes();
        this.outBytes = 2 * pairing.getGT().getLengthInBytes() +
                pairing.getG1().getLengthInBytes() +
                (length * 3 * pairing.getG1().getLengthInBytes());
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof UHIBELW11SecretKeyParameters) {
            // Decrypt
            UHIBELW11SecretKeyParameters sk = (UHIBELW11SecretKeyParameters) key;

            // Convert bytes to Elements...
            int offset = inOff;

            // Load C
            Element C = pairing.getGT().newElement();
            offset += C.setFromBytes(in, offset);

            // Load C0
            Element C0 = pairing.getG1().newElement();
            offset += C0.setFromBytes(in, offset);

            // Run the decryption
            Element numerator = pairing.getGT().newOneElement();
            Element denominator = pairing.getGT().newOneElement();
            for (int i = 0; i < length; i++) {
                Element C1 = pairing.getG1().newElement();
                offset += C1.setFromBytes(in, offset);

                Element C2 = pairing.getG1().newElement();
                offset += C2.setFromBytes(in, offset);

                Element C3 = pairing.getG1().newElement();
                offset += C3.setFromBytes(in, offset);

                numerator.mul(pairing.pairing(C0, sk.getK0At(i))).mul(pairing.pairing(C2, sk.getK2At(i)));
                denominator.mul(pairing.pairing(C1, sk.getK1At(i))).mul(pairing.pairing(C3, sk.getK3At(i)));
            }
            Element M = C.div(numerator.div(denominator));

            return M.toBytes();
        } else {
            // Load the message from in
            Element M = pairing.getGT().newRandomElement().getImmutable();

            // Encrypt the message under the specified attributes and convert to byte array
            UHIBELW11EncryptionParameters encParams = (UHIBELW11EncryptionParameters) key;
            UHIBELW11PublicKeyParameters pk = encParams.getPublicKey();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
            try {
                Element s = pairing.getZr().newRandomElement();

                Element C = M.mul(pk.getOmega().powZn(s));
                Element C0 = pk.getG().powZn(s);

                bytes.write(M.toBytes());
                bytes.write(C.toBytes());
                bytes.write(C0.toBytes());

                for (int i = 0; i < length; i++) {
                    Element t = pairing.getZr().newRandomElement();

                    // Computes C_{i,1}, C_{i,2}, C_{i,3} and writes them to the byte stream
                    Element C1 = pk.getW().powZn(s).mul(pk.getV().powZn(t));
                    Element C2 = pk.getG().powZn(t);
                    Element C3 = pk.getU().powZn(encParams.getIdAt(i)).mul(pk.getH()).powZn(t);

                    bytes.write(C1.toBytes());
                    bytes.write(C2.toBytes());
                    bytes.write(C3.toBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return bytes.toByteArray();
        }
    }


}
