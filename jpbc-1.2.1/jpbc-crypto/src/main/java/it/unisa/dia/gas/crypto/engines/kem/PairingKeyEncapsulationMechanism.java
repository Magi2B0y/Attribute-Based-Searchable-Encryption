package it.unisa.dia.gas.crypto.engines.kem;

import it.unisa.dia.gas.crypto.engines.PairingAsymmetricBlockCipher;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class PairingKeyEncapsulationMechanism extends PairingAsymmetricBlockCipher implements KeyEncapsulationMechanism {
    
    protected int keyBytes = 0;

    public int getKeyBlockSize() {
        return keyBytes;
    }

    public int getInputBlockSize() {
        if (forEncryption)
            return 0;

        return outBytes - keyBytes;
    }

    public int getOutputBlockSize() {
        if (forEncryption)
            return outBytes;

        return keyBytes;
    }

}
