package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class EDFA {

    private DFA dfa;

    private Element[] kStarts;
    private Map<DFA.Transition, Element[]> kTransitions;
    private Map<Integer, Element[]> kEnds;

    public EDFA(DFA dfa) {
        this.dfa = dfa;

        this.kStarts = new Element[2];
        this.kTransitions = new HashMap<DFA.Transition, Element[]>();
        this.kEnds = new HashMap<Integer, Element[]>();
    }

    public void setKStart(Element k1, Element k2) {
        kStarts[0] = k1.getImmutable();
        kStarts[1] = k2.getImmutable();
    }

    public void addKTransition(DFA.Transition transition, Element k1, Element k2, Element k3) {
        kTransitions.put(transition, new Element[]{k1.getImmutable(), k2.getImmutable(), k3.getImmutable()});
    }

    public void addKEnds(int state, Element k1, Element k2) {
        kEnds.put(state, new Element[]{k1.getImmutable(), k2.getImmutable()});
    }

    public Element accept(Pairing pairing, String w, Element[] wEnc) {
        // Init
        int index = 0;
        Element B = pairing.pairing(wEnc[index++], kStarts[0]).mul(
                pairing.pairing(wEnc[index++], kStarts[1]).invert()
        );

        // Run
        int currentState = 0; // Initial state

        for (int i = 0; i < w.length(); i++) {
            DFA.Transition transition = dfa.getTransition(currentState, w.charAt(i));
            Element[] kTransition = kTransitions.get(transition);

            B.mul(pairing.pairing(wEnc[index - 2], kTransition[0]))
                    .mul(pairing.pairing(wEnc[index++], kTransition[2]))
                    .mul(pairing.pairing(wEnc[index++], kTransition[1]).invert());

            currentState = transition.getTo();
        }

        // Finalize
        Element[] kEnd = kEnds.get(currentState);
        if (kEnd == null)
            return pairing.getGT().newOneElement();

        B.mul(pairing.pairing(wEnc[index++], kEnd[0]).invert())
                .mul(pairing.pairing(wEnc[index], kEnd[1]));

        return B;
    }
}
