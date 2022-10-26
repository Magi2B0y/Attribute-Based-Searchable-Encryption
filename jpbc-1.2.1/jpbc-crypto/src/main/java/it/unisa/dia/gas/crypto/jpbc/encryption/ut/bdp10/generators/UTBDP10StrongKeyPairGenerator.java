package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators;

import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTBDP10StrongKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private UTBDP10StrongKeyGenerationParameters param;

    private Pairing pairing;

    public void init(KeyGenerationParameters param) {
        this.param = (UTBDP10StrongKeyGenerationParameters) param;
        pairing = PairingFactory.getPairing(this.param.getParameters().getPublicParameters().getCurveParams());
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        UTBDP10StrongParameters parameters = param.getParameters();

        UTBDP10StrongPublicParameters publicParameters = parameters.getPublicParameters();
        UTBDP10StrongMasterSecretKeyParameters mskParameters = parameters.getMasterSecretKeyParameters();

        Element r = pairing.getZr().newElement().setToRandom();
        Element pk = publicParameters.getG1().powZn(r).mul(publicParameters.getG0());

        Element D0 = publicParameters.getG().powZn(
                r.duplicate().mul(mskParameters.getT1())
                        .mul(mskParameters.getT2())
                        .mul(mskParameters.getT3())
        );

        Element D1 = publicParameters.getG().powZn(
                mskParameters.getOmega().duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT3()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT3()).negate()
                )
        );

        Element D2 = publicParameters.getG().powZn(
                mskParameters.getOmega().duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT2()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT2()).negate()
                )
        );

        Element D3 = publicParameters.getG().powZn(
                mskParameters.getOmega().duplicate().mul(mskParameters.getT2()).mul(mskParameters.getT3()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(mskParameters.getT2()).mul(mskParameters.getT3()).negate()
                )
        );

        return new AsymmetricCipherKeyPair(
                new UTBDP10StrongPublicKeyParameters(publicParameters,
                                                  pk.getImmutable()),
                new UTBDP10StrongPrivateKeyParameters(publicParameters,
                                                   D0.getImmutable(), D1.getImmutable(),
                                                   D2.getImmutable(), D3.getImmutable(),
                                                   parameters.getRPublicParameters().getRPrivateKey())
        );
    }

}