package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11PublicKeyParameters extends UHIBELW11KeyParameters {

    private Element g;
    private Element u;
    private Element h;
    private Element v;
    private Element w;
    private Element omega;


    public UHIBELW11PublicKeyParameters(CurveParameters curveParameters,
                                        Element g, Element u, Element h,
                                        Element v, Element w,
                                        Element omega) {
        super(false, curveParameters);

        this.g = g.getImmutable();
        this.u = u.getImmutable();
        this.h = h.getImmutable();
        this.v = v.getImmutable();
        this.w = w.getImmutable();
        this.omega = omega.getImmutable();
    }

    
    public Element getG() {
        return g;
    }

    public Element getU() {
        return u;
    }

    public Element getH() {
        return h;
    }

    public Element getV() {
        return v;
    }

    public Element getW() {
        return w;
    }

    public Element getOmega() {
        return omega;
    }

}
