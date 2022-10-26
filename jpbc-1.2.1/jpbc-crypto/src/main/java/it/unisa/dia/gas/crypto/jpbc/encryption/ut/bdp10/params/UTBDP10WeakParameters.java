package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10WeakParameters implements CipherParameters {
    private UTBDP10WeakPublicParameters publicParameters;
    private UTBDP10WeakMasterSecretKeyParameters masterSecretKeyParameters;


    public UTBDP10WeakParameters(UTBDP10WeakPublicParameters publicParameters, UTBDP10WeakMasterSecretKeyParameters masterSecretKeyParameters) {
        this.publicParameters = publicParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
    }


    public UTBDP10WeakPublicParameters getPublicParameters() {
        return publicParameters;
    }

    public UTBDP10WeakMasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }
}

