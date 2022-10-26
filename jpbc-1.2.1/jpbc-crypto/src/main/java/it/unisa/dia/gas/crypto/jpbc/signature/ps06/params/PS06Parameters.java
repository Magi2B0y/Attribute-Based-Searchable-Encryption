package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PS06Parameters implements CipherParameters {
    private CurveParameters curveParams;
    private Element g;
    private int nU, nM;


    public PS06Parameters(CurveParameters curveParams, Element g, int nU, int nM) {
        this.curveParams = curveParams;
        this.g = g;
        this.nU = nU;
        this.nM = nM;
    }


    public CurveParameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

    public int getnU() {
        return nU;
    }

    public int getnM() {
        return nM;
    }

}