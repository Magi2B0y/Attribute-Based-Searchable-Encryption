package it.unisa.dia.gas.crypto.engines;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class PredicateOnlyPairingAsymmetricBlockCipher extends PairingAsymmetricBlockCipher {

    /**
     * Return the maximum size for an input block to this engine.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize() {
        if (forEncryption)
            return 0;

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

        return 1;
    }

}
