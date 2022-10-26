package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongPublicParameters implements CipherParameters {
    private CurveParameters curveParams;
    private Element g;
    private Element g0, g1;
    private Element omega;
    private Element T1, T2, T3;

    private CipherParameters rPublicKey;


    public UTBDP10StrongPublicParameters(CurveParameters curveParams,
                                         Element g, Element g0, Element g1, Element omega, Element T1, Element T2, Element T3,
                                         CipherParameters rPublicKey) {
        this.curveParams = curveParams;
        this.g = g;
        this.g0 = g0;
        this.g1 = g1;
        this.omega = omega;
        this.T1 = T1;
        this.T2 = T2;
        this.T3 = T3;

        this.rPublicKey = rPublicKey;
    }


    public CurveParameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

    public Element getG0() {
        return g0;
    }

    public Element getG1() {
        return g1;
    }

    public Element getOmega() {
        return omega;
    }

    public Element getT1() {
        return T1;
    }

    public Element getT2() {
        return T2;
    }

    public Element getT3() {
        return T3;
    }


    public CipherParameters getRPublicKey() {
        return rPublicKey;
    }
}