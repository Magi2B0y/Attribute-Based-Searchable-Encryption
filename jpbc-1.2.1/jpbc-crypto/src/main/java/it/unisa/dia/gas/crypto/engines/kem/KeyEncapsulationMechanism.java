package it.unisa.dia.gas.crypto.engines.kem;

import org.bouncycastle.crypto.AsymmetricBlockCipher;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface KeyEncapsulationMechanism extends AsymmetricBlockCipher{

    public int getKeyBlockSize();

}
