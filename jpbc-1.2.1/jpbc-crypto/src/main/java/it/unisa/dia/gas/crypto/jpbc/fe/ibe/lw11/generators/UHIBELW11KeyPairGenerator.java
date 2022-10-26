package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtils;
import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private UHIBELW11KeyPairGenerationParameters parameters;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.parameters = (UHIBELW11KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        DefaultCurveParameters curveParameters;
        Pairing pairing;
        Element g;

        // Generate curve parameters
        while (true) {
            curveParameters = generateCurveParams();
            pairing = PairingFactory.getPairing(curveParameters);

            Element generator = pairing.getG1().newRandomElement();
            g = ElementUtils.getGenerator(pairing, generator, curveParameters, 0, 3).getImmutable();
            if (!pairing.pairing(g, g).isOne())
                break;
        }

        // Generate required elements
        Element u = ElementUtils.randomIn(pairing, g).getImmutable();
        Element h = ElementUtils.randomIn(pairing, g).getImmutable();
        Element v = ElementUtils.randomIn(pairing, g).getImmutable();
        Element w = ElementUtils.randomIn(pairing, g).getImmutable();

        Element alpha = pairing.getZr().newRandomElement().getImmutable();

        Element omega = pairing.pairing(g, g).powZn(alpha).getImmutable();

        // Remove factorization from curveParams
        curveParameters.remove("n0");
        curveParameters.remove("n1");
        curveParameters.remove("n2");

        // Return the keypair

        return new AsymmetricCipherKeyPair(
                new UHIBELW11PublicKeyParameters(curveParameters, g, u, h, v, w, omega),
                new UHIBELW11MasterSecretKeyParameters(curveParameters, alpha)
        );
    }


    private DefaultCurveParameters generateCurveParams() {
        CurveGenerator curveGenerator = new TypeA1CurveGenerator(3, parameters.getBitLength());
        return (DefaultCurveParameters) curveGenerator.generate();
    }
}
