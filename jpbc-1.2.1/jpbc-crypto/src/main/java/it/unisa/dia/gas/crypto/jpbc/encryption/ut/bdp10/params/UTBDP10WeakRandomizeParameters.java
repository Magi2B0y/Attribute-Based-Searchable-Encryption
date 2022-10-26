package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10WeakRandomizeParameters extends UTBDP10WeakKeyParameters {

    public UTBDP10WeakRandomizeParameters(UTBDP10WeakPublicParameters publicParameters) {
        super(false, publicParameters);
    }

}
