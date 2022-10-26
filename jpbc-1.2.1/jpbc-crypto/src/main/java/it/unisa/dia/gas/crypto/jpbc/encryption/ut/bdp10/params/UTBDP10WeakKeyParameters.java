package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10WeakKeyParameters extends AsymmetricKeyParameter {
    private UTBDP10WeakPublicParameters publicParameters;


    public UTBDP10WeakKeyParameters(boolean isPrivate, UTBDP10WeakPublicParameters publicParameters) {
        super(isPrivate);
        this.publicParameters = publicParameters;
    }


    public UTBDP10WeakPublicParameters getParameters() {
        return publicParameters;
    }


}