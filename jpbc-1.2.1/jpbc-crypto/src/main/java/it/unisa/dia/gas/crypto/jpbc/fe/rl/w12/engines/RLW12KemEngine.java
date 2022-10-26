package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import it.unisa.dia.gas.crypto.engines.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12KemEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof RLW12EncryptionParameters))
                throw new IllegalArgumentException("RLW12EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof RLW12SecretKeyParameters))
                throw new IllegalArgumentException("RLW12SecretKeyParameters are required for decryption.");
        }

        RLW12KeyParameters rlKey = (RLW12KeyParameters) key;
        this.pairing = PairingFactory.getPairing(rlKey.getParameters().getCurveParameters());

        this.keyBytes = pairing.getGT().getLengthInBytes();
//        this.outBytes = 2 * pairing.getGT().getLengthInBytes() + N * pairing.getG1().getLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof RLW12SecretKeyParameters) {
            // Decrypt
            RLW12SecretKeyParameters secretKeyParameters = (RLW12SecretKeyParameters) key;

            // Read w
            int length = in[inOff++];
            String w = new String(in, inOff, length);
            inOff += length;

            // Read the ciphertext
            int l = (inLen - pairing.getGT().getLengthInBytes()) / pairing.getG1().getLengthInBytes();

            Element[] wEnc = new Element[l];
            for (int i = 0; i < l; i += 2) {
                wEnc[i] = pairing.getG1().newElement();
                inOff += wEnc[i].setFromBytes(in, inOff);

                wEnc[i + 1] = pairing.getG1().newElement();
                inOff += wEnc[i + 1].setFromBytes(in, inOff);
            }

            Element cm = pairing.getGT().newElement();
            inOff += cm.setFromBytes(in, inOff);

            // Run the decryption
            Element mask = secretKeyParameters.getEdfa().accept(pairing, w, wEnc);
            Element M = cm.div(mask);

            return M.toBytes();
        } else {
            Element M = pairing.getGT().newRandomElement();

            // Encrypt the massage under the specified attributes
            RLW12EncryptionParameters encKey = (RLW12EncryptionParameters) key;
            RLW12PublicKeyParameters publicKey = encKey.getPublicKey();
            String w = encKey.getW();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
            try {
                // Store M
                bytes.write(M.toBytes());

                // Store the ciphertext

                // Store w
                bytes.write(w.length());
                bytes.write(w.getBytes());

                // Initialize
                Element s0 = pairing.getZr().newRandomElement();
                bytes.write(
                        publicKey.getParameters().getG().powZn(s0).toBytes()
                );
                bytes.write(
                        publicKey.gethStart().powZn(s0).toBytes()
                );

                // Sequence
                Element sPrev = s0;
                for (int i = 0, l = w.length(); i < l; i++) {
                    Element sNext = pairing.getZr().newRandomElement();

                    bytes.write(
                            publicKey.getParameters().getG().powZn(sNext).toBytes()
                    );
                    bytes.write(
                            publicKey.getHAt(w.charAt(i)).powZn(sNext)
                                    .mul(publicKey.getZ().powZn(sPrev)).toBytes()
                    );

                    sPrev = sNext;
                }

                // Finalize
                bytes.write(
                        publicKey.getParameters().getG().powZn(sPrev).toBytes()
                );
                bytes.write(
                        publicKey.gethEnd().powZn(sPrev).toBytes()
                );

                // Store the masked message
                bytes.write(publicKey.getOmega().powZn(sPrev).mul(M).toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bytes.toByteArray();
        }
    }


}