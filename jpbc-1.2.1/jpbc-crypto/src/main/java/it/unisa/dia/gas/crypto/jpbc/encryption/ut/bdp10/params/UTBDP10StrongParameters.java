package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongParameters implements CipherParameters {
    private UTBDP10StrongPublicParameters publicParameters;
    private UTBDP10StrongRPublicParameters rPublicParameters;

    private UTBDP10StrongMasterSecretKeyParameters masterSecretKeyParameters;


    public UTBDP10StrongParameters(UTBDP10StrongPublicParameters publicParameters,
                                   UTBDP10StrongRPublicParameters rPublicParameters,
                                   UTBDP10StrongMasterSecretKeyParameters masterSecretKeyParameters) {
        this.publicParameters = publicParameters;
        this.rPublicParameters = rPublicParameters;

        this.masterSecretKeyParameters = masterSecretKeyParameters;
    }


    public UTBDP10StrongPublicParameters getPublicParameters() {
        return publicParameters;
    }

    public UTBDP10StrongRPublicParameters getRPublicParameters() {
        return rPublicParameters;
    }

    public UTBDP10StrongMasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }


}
