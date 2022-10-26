package it.unisa.dia.gas.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface CipherParametersGenerator {

    public void init(KeyGenerationParameters keyGenerationParameters);

    public CipherParameters generateKey();

}
