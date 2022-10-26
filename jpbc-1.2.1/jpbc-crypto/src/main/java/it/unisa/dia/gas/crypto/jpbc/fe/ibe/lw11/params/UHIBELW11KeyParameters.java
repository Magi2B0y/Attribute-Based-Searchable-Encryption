package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11KeyParameters extends AsymmetricKeyParameter {

    private CurveParameters curveParameters;


    public UHIBELW11KeyParameters(boolean isPrivate, CurveParameters curveParameters) {
        super(isPrivate);
        this.curveParameters = curveParameters;
    }


    public CurveParameters getCurveParameters() {
        return curveParameters;
    }

}


