package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10WeakKeyGenerationParameters extends KeyGenerationParameters {

    private UTBDP10WeakParameters params;

    public UTBDP10WeakKeyGenerationParameters(SecureRandom random, UTBDP10WeakParameters params) {
        super(random, params.getPublicParameters().getG().getField().getLengthInBytes());

        this.params = params;
    }

    public UTBDP10WeakParameters getParameters() {
        return params;
    }

}
