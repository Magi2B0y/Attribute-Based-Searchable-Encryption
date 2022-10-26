package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongRandomizeParameters extends UTBDP10StrongKeyParameters {
    private UTBDP10StrongRPublicParameters rPublicParameters;


    public UTBDP10StrongRandomizeParameters(UTBDP10StrongPublicParameters publicParameters,
                                            UTBDP10StrongRPublicParameters rPublicParameters) {
        super(true, publicParameters);
        this.rPublicParameters = rPublicParameters;
    }


    public UTBDP10StrongRPublicParameters getRPublicParameters() {
        return rPublicParameters;
    }


}
