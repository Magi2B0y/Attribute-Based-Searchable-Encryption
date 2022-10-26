package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10;

import it.unisa.dia.gas.crypto.engines.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.engines.UTBDP10StrongKEMEngine;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators.UTBDP10StrongKeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators.UTBDP10StrongParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.*;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.junit.Test;

import java.io.DataInputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongKEMEngineTest extends AbstractJPBCCryptoTest {


    public UTBDP10StrongKEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testUTBDP10StrongKEMEngine() {
        UTBDP10StrongParameters parameters = createParameters("it/unisa/dia/gas/crypto/jpbc/encryption/ut/bdp10/elgamal.param");
        AsymmetricCipherKeyPair keyPair = setup(parameters);

        byte[][] ct = encaps(keyPair.getPublic());

        assertEquals(true, Arrays.equals(ct[0], decaps(keyPair.getPrivate(), ct[1])));
        assertEquals(true, Arrays.equals(ct[0], decaps(keyPair.getPrivate(),
                randomize(parameters.getPublicParameters(), parameters.getRPublicParameters(), ct[1]))));
    }


    protected UTBDP10StrongParameters createParameters(int elgamalLength) {
        ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
        elGamalParametersGenerator.init(elgamalLength, 12, new SecureRandom());
        ElGamalParameters elGamalParameters = elGamalParametersGenerator.generateParameters();

        UTBDP10StrongParametersGenerator generator = new UTBDP10StrongParametersGenerator();
        generator.init(curveParameters, elGamalParameters);
        return generator.generateParameters();
    }

    protected UTBDP10StrongParameters createParameters(String elgamalParamsPath) {
        ElGamalParameters elGamalParameters;

        try {
            DataInputStream din = new DataInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(elgamalParamsPath));
            int len = din.readInt();
            byte[] buffer = new byte[len];
            din.readFully(buffer);
            BigInteger g = new BigInteger(buffer);

            len = din.readInt();
            buffer = new byte[len];
            din.readFully(buffer);
            BigInteger p = new BigInteger(buffer);

            int l = din.readInt();

            din.close();

            elGamalParameters = new ElGamalParameters(p, g, l);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        UTBDP10StrongParametersGenerator generator = new UTBDP10StrongParametersGenerator();
        generator.init(curveParameters, elGamalParameters);
        return generator.generateParameters();
    }
    
    protected AsymmetricCipherKeyPair setup(UTBDP10StrongParameters parameters) {
        UTBDP10StrongKeyPairGenerator setup = new UTBDP10StrongKeyPairGenerator();
        setup.init(new UTBDP10StrongKeyGenerationParameters(new SecureRandom(), parameters));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey) {
        try {
            KeyEncapsulationMechanism kem = new UTBDP10StrongKEMEngine();
            kem.init(true, publicKey);

            byte[] ciphertext = kem.processBlock(new byte[0], 0, 0);

            assertNotNull(ciphertext);
            assertNotSame(0, ciphertext.length);

            byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
            byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

            return new byte[][]{key, ct};
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    protected byte[] decaps(CipherParameters privateKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism engine = new UTBDP10StrongKEMEngine();
            engine.init(false, privateKey);

            return engine.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected byte[] randomize(UTBDP10StrongPublicParameters publicParameters,
                               UTBDP10StrongRPublicParameters rPublicParameters,
                               byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism engine = new UTBDP10StrongKEMEngine();
            engine.init(true, new UTBDP10StrongRandomizeParameters(publicParameters, rPublicParameters));

            return engine.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}