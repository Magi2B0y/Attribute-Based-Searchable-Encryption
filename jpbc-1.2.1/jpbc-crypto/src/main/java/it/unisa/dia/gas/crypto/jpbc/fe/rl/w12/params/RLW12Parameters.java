package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines.Alphabet;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12Parameters implements CipherParameters {
    private CurveParameters curveParameters;
    private Element g;
    private Alphabet alphabet;


    public RLW12Parameters(CurveParameters curveParameters, Element g, Alphabet alphabet) {
        this.curveParameters = curveParameters;
        this.g = g.getImmutable();
        this.alphabet = alphabet;
    }


    public CurveParameters getCurveParameters() {
        return curveParameters;
    }

    public Element getG() {
        return g;
    }

    public int getAlphabetSize() {
        return alphabet.getSize();
    }

    public int getCharacterIndex(Character character) {
        return alphabet.getIndex(character);
    }

}