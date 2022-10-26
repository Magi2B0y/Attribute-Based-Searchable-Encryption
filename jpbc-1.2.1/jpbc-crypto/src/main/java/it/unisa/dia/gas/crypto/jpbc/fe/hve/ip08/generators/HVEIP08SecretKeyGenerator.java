package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPow;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08SecretKeyGenerator {
    protected HVEIP08SecretKeyGenerationParameters param;
    protected int[] pattern;

    public void init(KeyGenerationParameters param) {
        this.param = (HVEIP08SecretKeyGenerationParameters) param;
        pattern = this.param.getPattern();

        if (pattern == null)
            throw new IllegalArgumentException("pattern cannot be null.");

        int n = this.param.getMasterSecretKey().getParameters().getN();
        if (pattern.length != n)
            throw new IllegalArgumentException("pattern length not valid.");
    }

    public CipherParameters generateKey() {
        HVEIP08MasterSecretKeyParameters masterSecretKey = param.getMasterSecretKey();
        if (param.isAllStar()) {
            return new HVEIP08SecretKeyParameters(
                    masterSecretKey.getParameters(),
                    masterSecretKey.getParameters().getElementPowG().powZn(masterSecretKey.getY())
            );
        }

        Pairing pairing = PairingFactory.getPairing(masterSecretKey.getParameters().getCurveParameters());

        int n = masterSecretKey.getParameters().getN();
        int numNonStar = param.getNumNonStar();

        // generate a_i's
        Element a[] = new Element[numNonStar];
        Element sum = pairing.getZr().newElement().setToZero();
        for (int i = 0; i < numNonStar - 1; i++) {
            a[i] = pairing.getZr().newElement().setToRandom();
            sum.add(a[i]);
        }
        a[numNonStar - 1] = masterSecretKey.getY().add(sum.negate());

        // generate key elements
        ElementPow g = masterSecretKey.getParameters().getElementPowG();

        Element[] Y = new Element[n];
        Element[] L = new Element[n];

        if (masterSecretKey.isPreProcessed()) {
            for (int i = 0, j=0; i < n; i++) {
                if (param.isStarAt(i)) {
                    Y[i] = null;
                    L[i] = null;
                } else {
                    Y[i] = g.powZn(a[j].duplicate().mul(masterSecretKey.getPreTAt(i, param.getPatternAt(i)))).getImmutable();
                    L[i] = g.powZn(a[j].duplicate().mul(masterSecretKey.getPreVAt(i, param.getPatternAt(i)))).getImmutable();
                    j++;
                }
            }
        } else {
            for (int i = 0, j=0; i < n; i++) {
                if (param.isStarAt(i)) {
                    Y[i] = null;
                    L[i] = null;
                } else {
                    Y[i] = g.powZn(a[j].duplicate().div(masterSecretKey.getTAt(i, param.getPatternAt(i)))).getImmutable();
                    L[i] = g.powZn(a[j].duplicate().div(masterSecretKey.getVAt(i, param.getPatternAt(i)))).getImmutable();
                    j++;
                }
            }
        }

        return new HVEIP08SecretKeyParameters(masterSecretKey.getParameters(), pattern, Y, L);
    }

}