package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongKeyGenerationParameters extends KeyGenerationParameters {

    private UTBDP10StrongParameters params;
    

    public UTBDP10StrongKeyGenerationParameters(SecureRandom random, UTBDP10StrongParameters params) {
        super(random, params.getPublicParameters().getG().getField().getLengthInBytes());

        this.params = params;
    }

    public UTBDP10StrongParameters getParameters() {
        return params;
    }

}