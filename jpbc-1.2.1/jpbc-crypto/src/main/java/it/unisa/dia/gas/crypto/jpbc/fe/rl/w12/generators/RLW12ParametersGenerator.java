package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines.Alphabet;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12Parameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12ParametersGenerator {
    private CurveParameters curveParameters;
    private Alphabet alphabet;

    private Pairing pairing;


    public RLW12ParametersGenerator init(CurveParameters curveParameters, Alphabet alphabet) {
        this.curveParameters = curveParameters;
        this.alphabet = alphabet;
        this.pairing = PairingFactory.getPairing(this.curveParameters);

        return this;
    }


    public RLW12Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new RLW12Parameters(curveParameters, g.getImmutable(), alphabet);
    }

}