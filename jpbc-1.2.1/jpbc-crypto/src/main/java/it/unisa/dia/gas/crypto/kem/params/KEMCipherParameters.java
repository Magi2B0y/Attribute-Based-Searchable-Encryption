package it.unisa.dia.gas.crypto.kem.params;

import org.bouncycastle.crypto.CipherParameters;

import java.security.AlgorithmParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class KEMCipherParameters implements CipherParameters {

    private AlgorithmParameters algorithmParameters;


    public KEMCipherParameters(AlgorithmParameters algorithmParameters) {
        this.algorithmParameters = algorithmParameters;
    }

    public AlgorithmParameters getAlgorithmParameters() {
        return algorithmParameters;
    }
}
