package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.engines.UHIBELW11PredicateOnlyEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.generators.UHIBELW11KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.generators.UHIBELW11SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro
 */
public class UHIBELW11PredicateOnlyEngineTest extends AbstractJPBCCryptoTest {


    public UHIBELW11PredicateOnlyEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testUHIBELW11PredicateOnlyEngine() {
        // Setup
        AsymmetricCipherKeyPair keyPair = setup(32);

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

        // Delegate and Test
        assertEquals(true, test(delegate(keyPair, sk0, ids[1]), ciphertext01));
        assertEquals(true, test(delegate(keyPair, sk01, ids[2]), ciphertext012));
        assertEquals(true, test(delegate(keyPair, delegate(keyPair, sk0, ids[1]), ids[2]), ciphertext012));

        assertEquals(false, test(delegate(keyPair, sk0, ids[2]), ciphertext01));
        assertEquals(false, test(delegate(keyPair, sk01, ids[1]), ciphertext012));
        assertEquals(false, test(delegate(keyPair, delegate(keyPair, sk0, ids[2]), ids[1]), ciphertext012));
    }


    protected AsymmetricCipherKeyPair setup(int bitLength) {
        UHIBELW11KeyPairGenerator setup = new UHIBELW11KeyPairGenerator();
        setup.init(new UHIBELW11KeyPairGenerationParameters(bitLength));

        return setup.generateKeyPair();
    }

    protected Element[] map(CipherParameters publicKey, String... ids) {
        Pairing pairing = PairingFactory.getPairing(((UHIBELW11PublicKeyParameters) publicKey).getCurveParameters());

        Element[] elements = new Element[ids.length];
        for (int i = 0; i < elements.length; i++) {
            byte[] id = ids[i].getBytes();
            elements[i] = pairing.getZr().newElement().setFromHash(id, 0, id.length);
        }
        return elements;
    }


    protected CipherParameters keyGen(AsymmetricCipherKeyPair masterKey, Element... ids) {
        UHIBELW11SecretKeyGenerator generator = new UHIBELW11SecretKeyGenerator();
        generator.init(new UHIBELW11SecretKeyGenerationParameters(
                (UHIBELW11MasterSecretKeyParameters) masterKey.getPrivate(),
                (UHIBELW11PublicKeyParameters) masterKey.getPublic(),
                ids
        ));

        return generator.generateKey();
    }

    protected CipherParameters delegate(AsymmetricCipherKeyPair masterKey, CipherParameters secretKey, Element id) {
        UHIBELW11SecretKeyGenerator generator = new UHIBELW11SecretKeyGenerator();
        generator.init(new UHIBELW11DelegateGenerationParameters(
                (UHIBELW11PublicKeyParameters) masterKey.getPublic(),
                (UHIBELW11SecretKeyParameters) secretKey,
                id
        ));

        return generator.generateKey();
    }

    protected byte[] encrypt(CipherParameters publicKey, Element... ids) {
        byte[] ciphertext = new byte[0];
        try {
            AsymmetricBlockCipher engine = new UHIBELW11PredicateOnlyEngine();
            engine.init(true, new UHIBELW11EncryptionParameters((UHIBELW11PublicKeyParameters) publicKey, ids));
            ciphertext = engine.processBlock(new byte[0], 0, 0);

            assertNotNull(ciphertext);
            assertNotSame(0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return ciphertext;
    }

    protected boolean test(CipherParameters secretKey, byte[] cipherText) {
        byte[] plainText = new byte[0];
        try {
            AsymmetricBlockCipher engine = new UHIBELW11PredicateOnlyEngine();
            // Decrypt
            engine.init(false, secretKey);
            plainText = engine.processBlock(cipherText, 0, cipherText.length);

            assertNotNull(plainText);
            assertNotSame(0, plainText.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return plainText[0] == 1; // Meaning that the predicate is satisfied
    }
}

