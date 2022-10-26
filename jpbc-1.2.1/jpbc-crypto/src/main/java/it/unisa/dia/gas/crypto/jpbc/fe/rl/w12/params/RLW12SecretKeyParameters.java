package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines.EDFA;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12SecretKeyParameters extends RLW12KeyParameters {
    private EDFA edfa;

    public RLW12SecretKeyParameters(RLW12Parameters parameters, EDFA edfa) {
        super(true, parameters);

        this.edfa = edfa;
    }

    public EDFA getEdfa() {
        return edfa;
    }
}