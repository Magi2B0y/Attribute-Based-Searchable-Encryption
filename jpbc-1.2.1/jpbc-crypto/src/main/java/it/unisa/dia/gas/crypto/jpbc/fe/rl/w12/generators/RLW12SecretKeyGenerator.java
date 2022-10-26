package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines.DFA;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines.EDFA;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12SecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12SecretKeyGenerator {
    private RLW12SecretKeyGenerationParameters param;

    private Pairing pairing;
    private DFA dfa;

    public void init(KeyGenerationParameters param) {
        this.param = (RLW12SecretKeyGenerationParameters) param;

        this.pairing = PairingFactory.getPairing(this.param.getMasterSecretKeyParameters().getParameters().getCurveParameters());
        this.dfa = this.param.getDfa();
    }

    public CipherParameters generateKey() {
        RLW12MasterSecretKeyParameters msk = param.getMasterSecretKeyParameters();
        RLW12PublicKeyParameters pk = param.getPublicKeyParameters();

        EDFA edfa = new EDFA(dfa);

        int ns = dfa.getNumStates();

        Element[] Ds = new Element[ns];
        for (int i = 0; i < ns; i++) {
            Ds[i] = pairing.getG1().newRandomElement().getImmutable();
        }

        // Initial state
        Element rs = pairing.getZr().newRandomElement();
        edfa.setKStart(Ds[0].mul(pk.gethStart().powZn(rs)), pk.getParameters().getG().powZn(rs));

        // Transitions
        for (int i = 0, size = dfa.getNumTransitions(); i < size; i++) {
            DFA.Transition transition = dfa.getTransitionAt(i);

            Element rt = pairing.getZr().newRandomElement();

            edfa.addKTransition(
                    transition,
                    Ds[transition.getFrom()].invert().mul(pk.getZ().powZn(rt)),
                    pk.getParameters().getG().powZn(rt),
                    Ds[transition.getTo()].mul(pk.getHAt(transition.getReading()).powZn(rt))
            );
        }

        // Final states
        Element secret = pk.getParameters().getG().powZn(msk.getAlpha().negate()).getImmutable();
        for (int i = 0, size = dfa.getNumFinalStates(); i < size; i++) {
            int finalState = dfa.getFinalStateAt(i);

            Element rf = pairing.getZr().newRandomElement();

            edfa.addKEnds(
                    finalState,
                    secret.mul(Ds[finalState]).mul(pk.gethEnd().powZn(rf)),
                    pk.getParameters().getG().powZn(rf)
            );
        }

        return new RLW12SecretKeyParameters(param.getPublicKeyParameters().getParameters(), edfa);
    }

}