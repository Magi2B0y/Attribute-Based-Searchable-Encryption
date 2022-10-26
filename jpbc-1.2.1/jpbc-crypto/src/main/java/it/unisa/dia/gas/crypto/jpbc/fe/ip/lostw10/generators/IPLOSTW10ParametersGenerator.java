package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10Parameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPLOSTW10ParametersGenerator {
    private CurveParameters curveParameters;
    private int n;

    private Pairing pairing;


    public IPLOSTW10ParametersGenerator init(CurveParameters curveParameters, int n) {
        this.curveParameters = curveParameters;
        this.n = n;
        this.pairing = PairingFactory.getPairing(this.curveParameters);

        return this;
    }


    public IPLOSTW10Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new IPLOSTW10Parameters(curveParameters, g.getImmutable(), n);
    }

}