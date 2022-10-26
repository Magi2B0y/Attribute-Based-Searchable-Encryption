package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08PredicateOnlyEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08PredicateOnlySecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro
 */
public class HVEIP08PredicateOnlyEngineTest extends HVEIP08AbstractTest {

    public HVEIP08PredicateOnlyEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testHVEIP08PredicateOnlyEngine() {
        int n = 5;
        AsymmetricCipherKeyPair keyPair = setup(genBinaryParam(n));

        int[][] vectors = createMatchingVectors(n);
        assertEquals(true, test(keyGen(keyPair.getPrivate(), vectors[0]), enc(keyPair.getPublic(), vectors[1])));

        vectors = createNonMatchingVectors(n);
        assertEquals(false, test(keyGen(keyPair.getPrivate(), vectors[0]), enc(keyPair.getPublic(), vectors[1])));
    }


    protected HVEIP08Parameters genBinaryParam(int n) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(n, curveParameters);

        return generator.generateParameters();
    }

    protected HVEIP08Parameters genParam(int... attributeLengths) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(curveParameters, attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        try {
            HVEIP08PredicateOnlyEngine engine = new HVEIP08PredicateOnlyEngine();
            engine.init(true, new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) publicKey, attributes));

            return engine.processBlock(new byte[0], 0, 0);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08PredicateOnlySecretKeyGenerator generator = new HVEIP08PredicateOnlySecretKeyGenerator();
        generator.init(new HVEIP08SecretKeyGenerationParameters(
                (HVEIP08MasterSecretKeyParameters) privateKey, pattern)
        );

        return generator.generateKey();
    }

    protected boolean test(CipherParameters searchKey, byte[] ct) {
        try {
            HVEIP08PredicateOnlyEngine engine = new HVEIP08PredicateOnlyEngine();
            engine.init(false, searchKey);

            return engine.processBlock(ct, 0, ct.length)[0] == 1; // Meaning that the predicate is satisfied
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }
}

