package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.engines.AHIBEDIP10PredicateOnlyEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBEDIP10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBEDIP10SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Angelo De Caro
 */
public class AHIBEDIP10PredicateOnlyEngineTest extends AbstractJPBCCryptoTest {


    public AHIBEDIP10PredicateOnlyEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testAHIBEDIP10PredicateOnlyEngine() {
        // Setup
        AsymmetricCipherKeyPair keyPair = setup(32, 10);

        // KeyGen
        Element[] ids = map(keyPair.getPublic(), "angelo", "de caro", "unisa");

        CipherParameters sk0 = keyGen(keyPair, ids[0]);
        CipherParameters sk01 = keyGen(keyPair, ids[0], ids[1]);
        CipherParameters sk012 = keyGen(keyPair, ids[0], ids[1], ids[2]);

        CipherParameters sk1 = keyGen(keyPair, ids[1]);
        CipherParameters sk10 = keyGen(keyPair, ids[1], ids[0]);
        CipherParameters sk021 = keyGen(keyPair, ids[0], ids[2], ids[1]);

        // Encrypt
        byte[] ciphertext0 = encrypt(keyPair.getPublic(), ids[0]);
        byte[] ciphertext01 = encrypt(keyPair.getPublic(), ids[0], ids[1]);
        byte[] ciphertext012 = encrypt(keyPair.getPublic(), ids[0], ids[1], ids[2]);

        // Test
        assertEquals(true, test(sk0, ciphertext0));
        assertEquals(true, test(sk01, ciphertext01));
        assertEquals(true, test(sk012, ciphertext012));

        assertEquals(false, test(sk1, ciphertext0));
        assertEquals(false, test(sk10, ciphertext01));
        assertEquals(false, test(sk021, ciphertext012));

        // Delegate/Test
        assertEquals(true, test(delegate(keyPair, sk0, ids[1]), ciphertext01));
        assertEquals(true, test(delegate(keyPair, sk01, ids[2]), ciphertext012));
        assertEquals(true, test(delegate(keyPair, delegate(keyPair, sk0, ids[1]), ids[2]), ciphertext012));

        assertEquals(false, test(delegate(keyPair, sk0, ids[0]), ciphertext01));
        assertEquals(false, test(delegate(keyPair, sk01, ids[1]), ciphertext012));
        assertEquals(false, test(delegate(keyPair, delegate(keyPair, sk0, ids[2]), ids[1]), ciphertext012));
    }


    protected AsymmetricCipherKeyPair setup(int bitLength, int length) {
        AHIBEDIP10KeyPairGenerator setup = new AHIBEDIP10KeyPairGenerator();
        setup.init(new AHIBEDIP10KeyPairGenerationParameters(bitLength, length));

        return setup.generateKeyPair();
    }

    protected Element[] map(CipherParameters publicKey, String... ids) {
        Pairing pairing = PairingFactory.getPairing(((AHIBEDIP10PublicKeyParameters) publicKey).getCurveParameters());

        Element[] elements = new Element[ids.length];
        for (int i = 0; i < elements.length; i++) {
            byte[] id = ids[i].getBytes();
            elements[i] = pairing.getZr().newElement().setFromHash(id, 0, id.length);
        }
        return elements;
    }


    protected CipherParameters keyGen(AsymmetricCipherKeyPair masterKey, Element... ids) {
        AHIBEDIP10SecretKeyGenerator generator = new AHIBEDIP10SecretKeyGenerator();
        generator.init(new AHIBEDIP10SecretKeyGenerationParameters(
                (AHIBEDIP10MasterSecretKeyParameters) masterKey.getPrivate(),
                (AHIBEDIP10PublicKeyParameters) masterKey.getPublic(),
                ids
        ));

        return generator.generateKey();
    }

    protected CipherParameters delegate(AsymmetricCipherKeyPair masterKey, CipherParameters secretKey, Element id) {
        AHIBEDIP10SecretKeyGenerator generator = new AHIBEDIP10SecretKeyGenerator();
        generator.init(new AHIBEDIP10DelegateGenerationParameters(
                (AHIBEDIP10PublicKeyParameters) masterKey.getPublic(),
                (AHIBEDIP10SecretKeyParameters) secretKey,
                id
        ));

        return generator.generateKey();
    }

    protected byte[] encrypt(CipherParameters publicKey, Element... ids) {
        byte[] ciphertext = new byte[0];

        try {
            AsymmetricBlockCipher engine = new AHIBEDIP10PredicateOnlyEngine();
            engine.init(true, new AHIBEDIP10EncryptionParameters((AHIBEDIP10PublicKeyParameters) publicKey, ids));
            ciphertext = engine.processBlock(new byte[0], 0, 0);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return ciphertext;
    }

    protected boolean test(CipherParameters secretKey, byte[] cipherText) {
        try {
            AsymmetricBlockCipher engine = new AHIBEDIP10PredicateOnlyEngine();
            engine.init(false, secretKey);
            return engine.processBlock(cipherText, 0, cipherText.length)[0] == 1; // Meaning that the predicate is satisfied.
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return false;
    }
}