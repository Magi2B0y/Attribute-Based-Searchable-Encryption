package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtils;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11SecretKeyParameters extends UHIBELW11KeyParameters {
    private Element[] K0s, K1s, K2s, K3s;
    private Element[] ids;

    public UHIBELW11SecretKeyParameters(CurveParameters curveParameters,
                                        Element[] K0s, Element[] K1s, Element[] K2s, Element[] K3s,
                                        Element[] ids) {
        super(true, curveParameters);

        this.K0s = ElementUtils.cloneImmutable(K0s);
        this.K1s = ElementUtils.cloneImmutable(K1s);
        this.K2s = ElementUtils.cloneImmutable(K2s);
        this.K3s = ElementUtils.cloneImmutable(K3s);

        this.ids = ElementUtils.cloneImmutable(ids);
    }


    public int getLength() {
        return ids.length;
    }

    public Element getIdAt(int index) {
        return ids[index];
    }

    public Element getK0At(int index) {
        return K0s[index];
    }

    public Element getK2At(int index) {
        return K2s[index];
    }

    public Element getK1At(int index) {
        return K1s[index];
    }

    public Element getK3At(int index) {
        return K3s[index];
    }

    public Element[] getIds() {
        return Arrays.copyOf(ids, ids.length);
    }
}
