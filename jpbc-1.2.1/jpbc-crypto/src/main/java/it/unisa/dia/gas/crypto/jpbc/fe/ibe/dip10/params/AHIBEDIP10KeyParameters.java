package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBEDIP10KeyParameters extends AsymmetricKeyParameter {

    private CurveParameters curveParameters;


    public AHIBEDIP10KeyParameters(boolean isPrivate, CurveParameters curveParameters) {
        super(isPrivate);
        this.curveParameters = curveParameters;
    }


    public CurveParameters getCurveParameters() {
        return curveParameters;
    }

}

