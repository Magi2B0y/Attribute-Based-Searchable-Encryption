package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators;

import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10WeakMasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10WeakParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10WeakPublicParameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10WeakParametersGenerator {

    private CurveParameters curveParams;

    private Pairing pairing;


    public void init(CurveParameters curveParams) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public UTBDP10WeakParameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();
        Element g0 = pairing.getG1().newElement().setToRandom();
        Element g1 = pairing.getG1().newElement().setToRandom();

        Element t1 = pairing.getZr().newElement().setToRandom();
        Element t2 = pairing.getZr().newElement().setToRandom();
        Element t3 = pairing.getZr().newElement().setToRandom();
        Element w  = pairing.getZr().newElement().setToRandom();

        Element omega = pairing.pairing(g, g).powZn(w.duplicate().mul(t1).mul(t2).mul(t3));
        Element T1 = g.duplicate().powZn(t1);
        Element T2 = g.duplicate().powZn(t2);
        Element T3 = g.duplicate().powZn(t3);

        UTBDP10WeakPublicParameters utmaPublicParameters = new UTBDP10WeakPublicParameters(curveParams, g, g0, g1, omega, T1, T2, T3);
        return new UTBDP10WeakParameters(
                utmaPublicParameters,
                new UTBDP10WeakMasterSecretKeyParameters(utmaPublicParameters, t1, t2, t3, w)
        );
    }

}
