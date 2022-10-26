package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators;

import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10StrongMasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10StrongParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10StrongPublicParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.UTBDP10StrongRPublicParameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongParametersGenerator {

    private CurveParameters curveParams;
    private AsymmetricCipherKeyPairGenerator rKeyPairGenerator;

    private Pairing pairing;


    public UTBDP10StrongParametersGenerator(AsymmetricCipherKeyPairGenerator rKeyPairGenerator) {
        this.rKeyPairGenerator = rKeyPairGenerator;
    }

    public UTBDP10StrongParametersGenerator() {
        this(new ElGamalKeyPairGenerator());
    }
    

    public void init(CurveParameters curveParams, KeyGenerationParameters keyGenerationParameters) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);

        rKeyPairGenerator.init(keyGenerationParameters);
    }

    public void init(CurveParameters curveParams, ElGamalParameters elGamalParameters) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);

        rKeyPairGenerator.init(new ElGamalKeyGenerationParameters(new SecureRandom(), elGamalParameters));
    }


    public UTBDP10StrongParameters generateParameters() {
        Element g = pairing.getG1().newRandomElement();
        Element g0 = pairing.getG1().newRandomElement();
        Element g1 = pairing.getG1().newRandomElement();

        // Values for the MSK
        Element t1 = pairing.getZr().newRandomElement();
        Element t2 = pairing.getZr().newRandomElement();
        Element t3 = pairing.getZr().newRandomElement();
        Element omega = pairing.getZr().newRandomElement();

        // Values for the PI
        Element T1 = g.duplicate().powZn(t1);
        Element T2 = g.duplicate().powZn(t2);
        Element T3 = g.duplicate().powZn(t3);
        Element Omega = pairing.pairing(g, g).powZn(omega.duplicate().mul(t1).mul(t2).mul(t3));

        AsymmetricCipherKeyPair rKeyPair = rKeyPairGenerator.generateKeyPair();

        UTBDP10StrongPublicParameters utmaPublicParameters = new UTBDP10StrongPublicParameters(
                curveParams,
                g.getImmutable(), g0.getImmutable(), g1.getImmutable(),
                Omega.getImmutable(),
                T1.getImmutable(), T2.getImmutable(), T3.getImmutable(),
                rKeyPair.getPublic());

        return new UTBDP10StrongParameters(
                utmaPublicParameters,
                new UTBDP10StrongRPublicParameters(rKeyPair.getPrivate()),
                new UTBDP10StrongMasterSecretKeyParameters(utmaPublicParameters,
                                                        t1.getImmutable(), t2.getImmutable(), t3.getImmutable(),
                                                        omega.getImmutable())
        );
    }

}