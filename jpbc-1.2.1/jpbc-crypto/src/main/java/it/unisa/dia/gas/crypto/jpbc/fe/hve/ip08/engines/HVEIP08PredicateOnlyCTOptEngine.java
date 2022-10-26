package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines;

import it.unisa.dia.gas.crypto.engines.PredicateOnlyPairingAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.DataLengthException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08PredicateOnlyCTOptEngine extends PredicateOnlyPairingAsymmetricBlockCipher {

    private int n;

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof HVEIP08EncryptionParameters) && !(key instanceof HVEIP08CiphertextPreprocessingParameters))
                throw new IllegalArgumentException("HVEIP08EncryptionParameters (HVEIP08SecretKeyParameters) are required for encryption (preprocessing).");
        } else {
            if (!(key instanceof HVEIP08SecretKeyParameters))
                throw new IllegalArgumentException("HVEIP08SecretKeyParameters required.");
        }

        HVEIP08KeyParameters hveKey = (HVEIP08KeyParameters) key;
        this.pairing = PairingFactory.getPairing(hveKey.getParameters().getCurveParameters());
        this.n = hveKey.getParameters().getN();

        if (key instanceof HVEIP08EncryptionParameters) {
            // encrypt
            this.inBytes = 0;
            this.outBytes = (2 * n) * pairing.getG1().getLengthInBytes();
        } else if (key instanceof HVEIP08SecretKeyParameters) {
            // decrypt
            this.inBytes = (2 * n) * pairing.getPairingPreProcessingLengthInBytes();
            this.outBytes = 1;
        } else {
            // pre-process
            this.inBytes = (2 * n) * pairing.getG1().getLengthInBytes();
            this.outBytes = (2 * n) * pairing.getPairingPreProcessingLengthInBytes();
        }
    }

    /**
     * Return the maximum size for an input block to this engine.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize() {
        if (forEncryption)
            return inBytes;

        return outBytes;
    }

    /**
     * Return the maximum size for an output block to this engine.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize() {
        if (forEncryption)
            return outBytes;

        return inBytes;
    }


    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof HVEIP08SecretKeyParameters) {
            // Test/Match

            // If the key is for all stars then the match predicate is
            // always satisfied.
            HVEIP08SecretKeyParameters secretKey = (HVEIP08SecretKeyParameters) key;
            if (secretKey.isAllStar())
                return new byte[]{1};

            // Convert bytes to Elements...
            int offset = inOff;

            // Load Xs, Ws..
            List<PairingPreProcessing> X = new ArrayList<PairingPreProcessing>(n);
            List<PairingPreProcessing> W = new ArrayList<PairingPreProcessing>(n);
            int pairingPreProcessingLengthInBytes = pairing.getPairingPreProcessingLengthInBytes();
            for (int i = 0; i < n; i++) {
                PairingPreProcessing x = pairing.pairing(in, offset);
                offset += pairingPreProcessingLengthInBytes;
                X.add(x);

                PairingPreProcessing w = pairing.pairing(in, offset);
                offset += pairingPreProcessingLengthInBytes;
                W.add(w);
            }

            // Run the rest
            Element result = pairing.getGT().newOneElement();
            for (int i = 0; i < secretKey.getParameters().getN(); i++) {
                if (!secretKey.isStar(i)) {
                    result.mul(
                            X.get(i).pairing(secretKey.getYAt(i))
                    ).mul(
                            W.get(i).pairing(secretKey.getLAt(i))
                    );
                }
            }

            return new byte[]{(byte) (result.isOne() ? 1 : 0)};
        } else if (key instanceof HVEIP08CiphertextPreprocessingParameters) {
            // Pre-process the ciphertext

            // Convert bytes to Elements...
            int offset = inOff;

            // Load Xs, Ws..
            ByteArrayOutputStream outputStream;
            try {
                outputStream = new ByteArrayOutputStream(getOutputBlockSize());
                for (int i = 0; i < n; i++) {
                    Element x = pairing.getG1().newElement();
                    offset += x.setFromBytes(in, offset);
                    outputStream.write(pairing.pairing(x).toBytes());

                    Element w = pairing.getG1().newElement();
                    offset += w.setFromBytes(in, offset);
                    outputStream.write(pairing.pairing(w).toBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return outputStream.toByteArray();
        } else {
            // Encryption
            if (inLen > inBytes || inLen < inBytes)
                throw new DataLengthException("input must be of size " + inBytes);

            // Encrypt the message under the specified attributes
            HVEIP08EncryptionParameters encParams = (HVEIP08EncryptionParameters) key;
            HVEIP08PublicKeyParameters pk = encParams.getPublicKey();

            Element s = pairing.getZr().newRandomElement().getImmutable();

            List<Element> elements = new ArrayList<Element>();
            for (int i = 0; i < n; i++) {
                Element si = pairing.getZr().newElement().setToRandom();
                Element sMinusSi = s.sub(si);

                int j = encParams.getAttributeAt(i);

                elements.add(pk.getElementPowTAt(i, j).powZn(sMinusSi));  // X_i
                elements.add(pk.getElementPowVAt(i, j).powZn(si));        // W_i
            }

            // Convert to byte array
            ByteArrayOutputStream outputStream;
            try {
                outputStream = new ByteArrayOutputStream(getOutputBlockSize());
                for (Element element : elements)
                    outputStream.write(element.toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return outputStream.toByteArray();
        }
    }


}