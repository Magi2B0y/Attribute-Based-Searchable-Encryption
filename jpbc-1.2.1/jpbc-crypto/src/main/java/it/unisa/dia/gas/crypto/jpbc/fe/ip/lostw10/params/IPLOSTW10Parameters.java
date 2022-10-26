package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPLOSTW10Parameters implements CipherParameters {
    private CurveParameters curveParameters;
    private Element g;
    private int n;


    public IPLOSTW10Parameters(CurveParameters curveParameters, Element g, int n) {
        this.curveParameters = curveParameters;
        this.g = g.getImmutable();
        this.n = n;
    }


    public CurveParameters getCurveParameters() {
        return curveParameters;
    }

    public Element getG() {
        return g;
    }

    public int getN() {
        return n;
    }

}