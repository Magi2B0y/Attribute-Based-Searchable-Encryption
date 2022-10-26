package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines.IPLOSTW10PredicateOnlyEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro
 */
public class IPLOSTW10PredicateOnlyEngineTest extends AbstractJPBCCryptoTest {


    public IPLOSTW10PredicateOnlyEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testIPLOSTW10PredicateOnlyEngine() {
        int n = 2;

        // Setup
        AsymmetricCipherKeyPair keyPair = setup(createParameters(n));

        // Encrypt
        Element[][] vectors = createOrthogonalVectors(keyPair.getPublic(), n);
        assertTrue(test(keyGen(keyPair.getPrivate(), vectors[1]), encrypt(keyPair.getPublic(), vectors[0])));

        // Gen non-matching SearchKey
        vectors = createNonOrthogonalVectors(keyPair.getPublic(), n);
        assertFalse(test(keyGen(keyPair.getPrivate(), vectors[1]), encrypt(keyPair.getPublic(), vectors[0])));
    }


    protected IPLOSTW10Parameters createParameters(int n) {
        return new IPLOSTW10ParametersGenerator().init(curveParameters, n).generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(IPLOSTW10Parameters parameters) {
        IPLOSTW10KeyPairGenerator setup = new IPLOSTW10KeyPairGenerator();
        setup.init(new IPLOSTW10KeyGenerationParameters(
                new SecureRandom(),
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected Element[][] createOrthogonalVectors(CipherParameters publicKey, int n) {
        Pairing pairing = PairingFactory.getPairing(((IPLOSTW10PublicKeyParameters) publicKey).getParameters().getCurveParameters());

        Element[][] result = new Element[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i += 2) {
            if (random.nextBoolean()) {
                result[0][i] = pairing.getZr().newZeroElement();
                result[0][i + 1] = pairing.getZr().newZeroElement();

                result[1][i] = pairing.getZr().newRandomElement();
                result[1][i + 1] = pairing.getZr().newRandomElement();
            } else {
                result[0][i] = pairing.getZr().newOneElement();
                result[0][i + 1] = pairing.getZr().newRandomElement();

                result[1][i] = result[0][i + 1].duplicate().negate();
                result[1][i + 1] = pairing.getZr().newOneElement();
            }
        }
        return result;
    }

    protected Element[][] createNonOrthogonalVectors(CipherParameters publicKey, int n) {
        Pairing pairing = PairingFactory.getPairing(((IPLOSTW10PublicKeyParameters) publicKey).getParameters().getCurveParameters());

        Element[][] result = new Element[2][n];
        for (int i = 0; i < n; i += 2) {
            result[0][i] = pairing.getZr().newOneElement();
            result[0][i + 1] = pairing.getZr().newRandomElement();

            result[1][i] = pairing.getZr().newOneElement().sub(result[0][i + 1]);
            result[1][i + 1] = pairing.getZr().newOneElement();
        }
        return result;
    }

    protected Element[] createRandom(Pairing pairing, int n) {
        Element[] result = new Element[n];
        for (int i = 0; i < n; i++)
            result[i] = pairing.getZr().newRandomElement();
        return result;
    }

    protected byte[] encrypt(CipherParameters publicKey, Element[] x) {
        try {
            IPLOSTW10PredicateOnlyEngine engine = new IPLOSTW10PredicateOnlyEngine();
            engine.init(true, new IPLOSTW10EncryptionParameters((IPLOSTW10PublicKeyParameters) publicKey,  x));

            return engine.processBlock(new byte[0], 0, 0);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected CipherParameters keyGen(CipherParameters privateKey, Element[] y) {
        IPLOSTW10SecretKeyGenerator keyGen = new IPLOSTW10SecretKeyGenerator();
        keyGen.init(new IPLOSTW10SecretKeyGenerationParameters(
                (IPLOSTW10MasterSecretKeyParameters) privateKey, y
        ));
        
        return keyGen.generateKey();
    }

    protected boolean test(CipherParameters secretKey, byte[] ciphertext) {
        try {
            IPLOSTW10PredicateOnlyEngine engine = new IPLOSTW10PredicateOnlyEngine();
            engine.init(false, secretKey);

            return engine.processBlock(ciphertext, 0, ciphertext.length)[0] == 1; // Meaning that the predicate is satisfied.
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

}