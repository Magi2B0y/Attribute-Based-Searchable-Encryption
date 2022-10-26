package it.unisa.dia.gas.crypto.jpbc;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assume.assumeTrue;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractJPBCCryptoTest {

    static {
        PairingFactory.getInstance().setReuseInstance(false);
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }


    protected String curvePath;
    protected boolean usePBC;

    protected CurveParameters curveParameters;

    public AbstractJPBCCryptoTest(boolean usePBC, String curvePath) {
        this.usePBC = usePBC;
        this.curvePath = curvePath;
    }

    @Before
    public void before() throws Exception {
        assumeTrue(!usePBC || PairingFactory.getInstance().isPBCAvailable());

        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);
        curveParameters = PairingFactory.getInstance().loadCurveParameters(curvePath);

        assumeTrue(curveParameters != null);
    }

}
