package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongPublicKeyParameters extends UTBDP10StrongKeyParameters {
    private Element pk;


    public UTBDP10StrongPublicKeyParameters(UTBDP10StrongPublicParameters publicParameters, Element pk) {
        super(false, publicParameters);
        
        this.pk = pk;
    }


    public Element getPk() {
        return pk;
    }

}