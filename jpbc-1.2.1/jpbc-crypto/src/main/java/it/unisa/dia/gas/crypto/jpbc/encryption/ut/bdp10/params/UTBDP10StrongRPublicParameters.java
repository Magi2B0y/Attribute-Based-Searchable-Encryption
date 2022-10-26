package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongRPublicParameters implements CipherParameters {
    private CipherParameters rPrivateKey;


    public UTBDP10StrongRPublicParameters(CipherParameters rPrivateKey) {
        this.rPrivateKey = rPrivateKey;
    }


    public CipherParameters getRPrivateKey() {
        return rPrivateKey;
    }
}