package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongKeyParameters extends AsymmetricKeyParameter {
    private UTBDP10StrongPublicParameters publicParameters;


    public UTBDP10StrongKeyParameters(boolean isPrivate, UTBDP10StrongPublicParameters publicParameters) {
        super(isPrivate);
        this.publicParameters = publicParameters;
    }


    public UTBDP10StrongPublicParameters getParameters() {
        return publicParameters;
    }

}