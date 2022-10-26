package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10WeakPublicKeyParameters extends UTBDP10WeakKeyParameters {
    private Element pk;

    public UTBDP10WeakPublicKeyParameters(UTBDP10WeakPublicParameters publicParameters, Element pk) {
        super(false, publicParameters);
        this.pk = pk;
    }

    public Element getPk() {
        return pk;
    }
}
