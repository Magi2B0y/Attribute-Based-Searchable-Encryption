package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08PredicateOnlyCTOptEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08PredicateOnlyEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08PredicateOnlySecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro
 */
public class HVEIP08PredicateOnlyBenchmark {

    public static void main(String[] args) {
        HVEIP08PredicateOnlyBenchmark benchmark = new HVEIP08PredicateOnlyBenchmark();
        benchmark.benchmark();
    }

    protected CurveParameters curveParameters;
    protected long elapsedCT, elapsedKey;


    public void benchmark() {
        PairingFactory.getInstance().setUsePBCWhenPossible(false);
        curveParameters = PairingFactory.getInstance().loadCurveParameters("./params/a_181_603.properties");

        int n = 20;
        AsymmetricCipherKeyPair keyPair = setup(genBinaryParam(n));
        CipherParameters parameters = ((HVEIP08KeyParameters) keyPair.getPublic()).getParameters();

        elapsedCT = elapsedKey = 0;
        int iteration = 20;
        for (int i = 0; i < iteration; i++) {
            System.out.print(".");

            int[][] vectors = createMatchingVectors(n);
            CipherParameters secretKey = keyGen(keyPair.getPrivate(), vectors[0]);

            testCT(
                    secretKey,
                    preprocessCT(parameters, encCT(keyPair.getPublic(), vectors[1]))
            );

            testKey(
                    secretKey,
                    encKey(keyPair.getPublic(), vectors[1])
            );

        }
        System.out.println();
        System.out.println("elapsedCT = " + elapsedCT/iteration);
        System.out.println("elapsedKey = " + elapsedKey/iteration);

    }


    protected int[][] createMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            if (random.nextBoolean()) {
                // it's a star
                result[0][i] = -1;
                result[1][i] = random.nextInt(2);
            } else {
                result[0][i] = random.nextInt(2);
                result[1][i] = result[0][i];
            }
        }
        return result;
    }

    protected int[][] createNonMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            result[0][i] = random.nextInt(2);
            result[1][i] = 1 - result[0][i];
        }
        return result;
    }

    protected Element[] createRandom(Pairing pairing, int n) {
        Element[] result = new Element[n];
        for (int i = 0; i < n; i++)
            result[i] = pairing.getZr().newRandomElement();
        return result;
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

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08PredicateOnlySecretKeyGenerator generator = new HVEIP08PredicateOnlySecretKeyGenerator();
        generator.init(new HVEIP08SecretKeyGenerationParameters(
                (HVEIP08MasterSecretKeyParameters) privateKey, pattern)
        );

        return generator.generateKey();
    }


    protected byte[] encKey(CipherParameters publicKey, int... attributes) {
        try {
            HVEIP08PredicateOnlyEngine engine = new HVEIP08PredicateOnlyEngine();
            engine.init(true, new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) publicKey, attributes));

            return engine.processBlock(new byte[0], 0, 0);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean testKey(CipherParameters searchKey, byte[] ct) {
        try {
            HVEIP08PredicateOnlyEngine engine = new HVEIP08PredicateOnlyEngine();
            engine.init(false, searchKey);

            long start = System.currentTimeMillis();
            try {
                return engine.processBlock(ct, 0, ct.length)[0] == 1; // Meaning that the predicate is satisfied
            } finally {
                long end = System.currentTimeMillis();
                elapsedKey += (end-start);
            }
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    protected byte[] encCT(CipherParameters publicKey, int... attributes) {
        try {
            HVEIP08PredicateOnlyCTOptEngine engine = new HVEIP08PredicateOnlyCTOptEngine();
            engine.init(true, new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) publicKey, attributes));

            return engine.processBlock(new byte[0], 0, 0);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    protected byte[] preprocessCT(CipherParameters parameters, byte[] enc) {
        try {
            HVEIP08PredicateOnlyCTOptEngine engine = new HVEIP08PredicateOnlyCTOptEngine();
            engine.init(true, new HVEIP08CiphertextPreprocessingParameters((HVEIP08Parameters) parameters));

            return engine.processBlock(enc, 0, enc.length);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }


    protected boolean testCT(CipherParameters searchKey, byte[] ct) {
        try {
            HVEIP08PredicateOnlyCTOptEngine engine = new HVEIP08PredicateOnlyCTOptEngine();
            engine.init(false, searchKey);

            long start = System.currentTimeMillis();
            try {
                return engine.processBlock(ct, 0, ct.length)[0] == 1; // Meaning that the predicate is satisfied
            } finally {
                long end = System.currentTimeMillis();
                elapsedCT += (end-start);
            }
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }
}

