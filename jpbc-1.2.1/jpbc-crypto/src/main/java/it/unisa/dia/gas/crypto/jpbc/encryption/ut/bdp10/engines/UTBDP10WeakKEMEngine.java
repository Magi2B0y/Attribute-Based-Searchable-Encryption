package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.engines;

import it.unisa.dia.gas.crypto.engines.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10WeakKEMEngine extends PairingKeyEncapsulationMechanism {
    protected boolean forRandomization;

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof UTBDP10WeakPublicKeyParameters) && !(key instanceof UTBDP10WeakRandomizeParameters))
                throw new IllegalArgumentException("UTBDP10WeakPublicKeyParameters are required for encryption/randomization.");
            forRandomization = key instanceof UTBDP10WeakRandomizeParameters;
        } else {
            if (!(key instanceof UTBDP10WeakPrivateKeyParameters))
                throw new IllegalArgumentException("UTBDP10WeakPrivateKeyParameters are required for decryption.");
        }

        UTBDP10WeakKeyParameters keyParameters = (UTBDP10WeakKeyParameters) key;

        this.pairing = PairingFactory.getPairing(keyParameters.getParameters().getCurveParams());

        if (key instanceof UTBDP10WeakRandomizeParameters) {
            this.inBytes = 8 * pairing.getG1().getLengthInBytes();
            this.outBytes = inBytes;
        } else {
            this.inBytes = 0;
            this.keyBytes = pairing.getGT().getLengthInBytes();
            this.outBytes = keyBytes + 8 * pairing.getG1().getLengthInBytes();
        }
    }

    public int getInputBlockSize() {
        if (forRandomization)
            return inBytes;

        return super.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        if (forRandomization)
            return outBytes;

        return super.getOutputBlockSize();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof UTBDP10WeakPrivateKeyParameters) {
            // Convert bytes to Elements...
            int offset = inOff;

            // load omega...
            Element C = pairing.getGT().newElement();
            offset += C.setFromBytes(in, offset);

            // load C0...
            Element C0 = pairing.getG1().newElement();
            offset += C0.setFromBytes(in, offset);

            // load C1...
            Element C1 = pairing.getG1().newElement();
            offset += C1.setFromBytes(in, offset);

            // load C2...
            Element C2 = pairing.getG1().newElement();
            offset += C2.setFromBytes(in, offset);

            // load C3...
            Element C3 = pairing.getG1().newElement();
            offset += C3.setFromBytes(in, offset);

            UTBDP10WeakPrivateKeyParameters privateKeyParameters = (UTBDP10WeakPrivateKeyParameters) key;

            C.mul(pairing.pairing(C0, privateKeyParameters.getD0()))
                    .mul(pairing.pairing(C1, privateKeyParameters.getD1()))
                    .mul(pairing.pairing(C2, privateKeyParameters.getD2()))
                    .mul(pairing.pairing(C3, privateKeyParameters.getD3()));
            return C.toBytes();
        } else if (key instanceof UTBDP10WeakPublicKeyParameters) {
            // encryption
            Element M = pairing.getGT().newRandomElement();

            // Convert the Elements to byte arrays
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream(getOutputBlockSize());
                out.write(M.toBytes());
                encrypt(out, M);
                encrypt(out, pairing.getGT().newOneElement());

                return out.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            UTBDP10WeakRandomizeParameters keyParameters = (UTBDP10WeakRandomizeParameters) key;

            int offset = inOff;
            Element[] ct = extractCipherText(in, offset);
            offset += (pairing.getGT().getLengthInBytes() + (4  * pairing.getG1().getLengthInBytes()));
            Element[] ct1 = extractCipherText(in, offset);

            Element r  = pairing.getZr().newElement().setToRandom();
            Element r2 = pairing.getZr().newElement().setToRandom();
            Element r3 = pairing.getZr().newElement().setToRandom();

            // Convert the Elements to byte arrays
            ct1 = star(keyParameters.getParameters(), ct1, r, r2, r3);
            ct = mulComponentWise(ct, ct1);
            ct1 = star(keyParameters.getParameters(), ct1, r, r2, r3);

            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
                for (Element e : ct)
                    bytes.write(e.toBytes());
                for (Element e : ct1)
                    bytes.write(e.toBytes());
                return bytes.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    protected void encrypt(ByteArrayOutputStream outputStream, Element M) {
        UTBDP10WeakPublicKeyParameters publicKeyParameters = (UTBDP10WeakPublicKeyParameters) key;

        Element s = pairing.getZr().newElement().setToRandom();
        Element s1 = pairing.getZr().newElement().setToRandom();
        Element s2 = pairing.getZr().newElement().setToRandom();

        Element C = publicKeyParameters.getParameters().getOmega().powZn(s).mul(M);
        Element C0 = publicKeyParameters.getPk().duplicate().mulZn(s);
        Element C1 = publicKeyParameters.getParameters().getT2().powZn(s2);
        Element C2 = publicKeyParameters.getParameters().getT3().powZn(s.duplicate().sub(s1).sub(s2));
        Element C3 = publicKeyParameters.getParameters().getT1().powZn(s1);

        // Convert the Elements to byte arrays
        try {
            outputStream.write(C.toBytes());
            outputStream.write(C0.toBytes());
            outputStream.write(C1.toBytes());
            outputStream.write(C2.toBytes());
            outputStream.write(C3.toBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Element[] extractCipherText(byte[] in, int inOff) {
        int offset = inOff;

        // load omega...
        Element C = pairing.getGT().newElement();
        offset += C.setFromBytes(in, offset);

        // load C0...
        Element C0 = pairing.getG1().newElement();
        offset += C0.setFromBytes(in, offset);

        // load C1...
        Element C1 = pairing.getG1().newElement();
        offset += C1.setFromBytes(in, offset);

        // load C2...
        Element C2 = pairing.getG1().newElement();
        offset += C2.setFromBytes(in, offset);

        // load C3...
        Element C3 = pairing.getG1().newElement();
        offset += C3.setFromBytes(in, offset);

        return new Element[]{C, C0, C1, C2, C3};
    }

    protected Element[] star(UTBDP10WeakPublicParameters pk, Element[] ct, Element r, Element r2, Element r3) {
        ct[0].powZn(r);
        ct[1].powZn(r);
        ct[2] = ct[2].powZn(r).mul(pk.getT2().powZn(r2));
        ct[3] = ct[3].powZn(r).mul(pk.getT3().powZn(r3));
        ct[4] = ct[4].powZn(r).mul(pk.getT1().powZn(r2.duplicate().add(r3).negate()));

        return ct;
    }

    protected Element[] mulComponentWise(Element[] ct1, Element[] ct2) {
        ct1[0].mul(ct2[0]);
        ct1[1].mul(ct2[1]);
        ct1[2].mul(ct2[2]);
        ct1[3].mul(ct2[3]);
        ct1[4].mul(ct2[4]);

        return ct1;
    }

}
