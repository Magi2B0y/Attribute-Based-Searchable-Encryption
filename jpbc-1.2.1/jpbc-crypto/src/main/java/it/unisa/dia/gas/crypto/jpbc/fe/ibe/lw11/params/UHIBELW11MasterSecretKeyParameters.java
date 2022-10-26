package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11MasterSecretKeyParameters extends UHIBELW11KeyParameters {

    private Element alpha;


    public UHIBELW11MasterSecretKeyParameters(CurveParameters curveParameters, Element alpha) {
        super(true, curveParameters);

        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
