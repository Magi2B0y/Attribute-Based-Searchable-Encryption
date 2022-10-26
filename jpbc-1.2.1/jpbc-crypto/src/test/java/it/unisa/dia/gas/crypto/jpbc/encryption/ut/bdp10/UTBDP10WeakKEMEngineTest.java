package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10;

import it.unisa.dia.gas.crypto.engines.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.engines.UTBDP10WeakKEMEngine;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators.UTBDP10WeakKeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators.UTBDP10WeakParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10WeakKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10WeakParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10WeakPublicParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10WeakRandomizeParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10WeakKEMEngineTest extends AbstractJPBCCryptoTest {


    public UTBDP10WeakKEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testUTBDP10WeakKEMEngine() {
        UTBDP10WeakParameters parameters = createParameters();
        AsymmetricCipherKeyPair keyPair = setup(parameters);

        byte[][] ct = encaps(keyPair.getPublic());

        assertEquals(true, Arrays.equals(ct[0], decaps(keyPair.getPrivate(), ct[1])));
        assertEquals(true, Arrays.equals(ct[0], decaps(keyPair.getPrivate(), randomize(parameters.getPublicParameters(),ct[1]))));
    }


    protected UTBDP10WeakParameters createParameters() {
        UTBDP10WeakParametersGenerator generator = new UTBDP10WeakParametersGenerator();
        generator.init(curveParameters);
        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(UTBDP10WeakParameters parameters) {
        UTBDP10WeakKeyPairGenerator setup = new UTBDP10WeakKeyPairGenerator();
        setup.init(new UTBDP10WeakKeyGenerationParameters(new SecureRandom(), parameters));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey) {
        try {
            KeyEncapsulationMechanism kem = new UTBDP10WeakKEMEngine();
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
            KeyEncapsulationMechanism engine = new UTBDP10WeakKEMEngine();
            engine.init(false, privateKey);

            return engine.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected byte[] randomize(UTBDP10WeakPublicParameters publicParameters, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism engine = new UTBDP10WeakKEMEngine();
            engine.init(true, new UTBDP10WeakRandomizeParameters(publicParameters));

            return engine.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
