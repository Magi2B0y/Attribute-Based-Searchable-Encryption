package it.unisa.dia.gas.plaf.jpbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ElementPowPreProcessingTest extends AbstractJPBCTest {

    protected Pairing.PairingFieldIdentifier fieldIdentifier;
    protected Field field;


    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties", Pairing.PairingFieldIdentifier.G1},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties", Pairing.PairingFieldIdentifier.G2},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties", Pairing.PairingFieldIdentifier.GT},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties", Pairing.PairingFieldIdentifier.Zr},

                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties", Pairing.PairingFieldIdentifier.G1},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties", Pairing.PairingFieldIdentifier.G2},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties", Pairing.PairingFieldIdentifier.GT},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties", Pairing.PairingFieldIdentifier.Zr},

                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties", Pairing.PairingFieldIdentifier.G1},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties", Pairing.PairingFieldIdentifier.G2},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties", Pairing.PairingFieldIdentifier.GT},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties", Pairing.PairingFieldIdentifier.Zr},

                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties", Pairing.PairingFieldIdentifier.G1},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties", Pairing.PairingFieldIdentifier.G2},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties", Pairing.PairingFieldIdentifier.GT},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties", Pairing.PairingFieldIdentifier.Zr},

                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties", Pairing.PairingFieldIdentifier.G1},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties", Pairing.PairingFieldIdentifier.G2},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties", Pairing.PairingFieldIdentifier.GT},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties", Pairing.PairingFieldIdentifier.Zr},

                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties", Pairing.PairingFieldIdentifier.G1},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties", Pairing.PairingFieldIdentifier.G2},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties", Pairing.PairingFieldIdentifier.GT},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties", Pairing.PairingFieldIdentifier.Zr},

                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties", Pairing.PairingFieldIdentifier.G1},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties", Pairing.PairingFieldIdentifier.G2},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties", Pairing.PairingFieldIdentifier.GT},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties", Pairing.PairingFieldIdentifier.Zr},

                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties", Pairing.PairingFieldIdentifier.G1},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties", Pairing.PairingFieldIdentifier.G2},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties", Pairing.PairingFieldIdentifier.GT},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties", Pairing.PairingFieldIdentifier.Zr},

                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties", Pairing.PairingFieldIdentifier.G1},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties", Pairing.PairingFieldIdentifier.G2},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties", Pairing.PairingFieldIdentifier.GT},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties", Pairing.PairingFieldIdentifier.Zr},

                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties", Pairing.PairingFieldIdentifier.G1},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties", Pairing.PairingFieldIdentifier.G2},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties", Pairing.PairingFieldIdentifier.GT},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties", Pairing.PairingFieldIdentifier.Zr},

                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties", Pairing.PairingFieldIdentifier.G1},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties", Pairing.PairingFieldIdentifier.G2},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties", Pairing.PairingFieldIdentifier.GT},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties", Pairing.PairingFieldIdentifier.Zr},

                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties", Pairing.PairingFieldIdentifier.G1},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties", Pairing.PairingFieldIdentifier.G2},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties", Pairing.PairingFieldIdentifier.GT},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties", Pairing.PairingFieldIdentifier.Zr}
        };

        return Arrays.asList(data);
    }


    public ElementPowPreProcessingTest(boolean usePBC, String curvePath, Pairing.PairingFieldIdentifier fieldIdentifier) {
        super(usePBC, curvePath);

        this.fieldIdentifier = fieldIdentifier;
    }

    @Before
    public void before() throws Exception {
        super.before();

        switch (fieldIdentifier) {
            case G1 :
                this.field = pairing.getG1();
                break;
            case G2 :
                this.field = pairing.getG2();
                break;
            case GT :
                this.field = pairing.getGT();
                break;
            case Zr :
                this.field = pairing.getZr();
                break;
            default:
                assumeTrue(false);
        }
    }

    @Test
    public void testPowPreProcessing() {
        Element base = field.newElement().setToRandom().getImmutable();

        BigInteger n = pairing.getZr().newElement().setToRandom().toBigInteger();

        Element r1 = base.pow().pow(n);
        Element r2 = base.pow(n);

        assertTrue(r1.isEqual(r2));
    }

    @Test
    public void testPowPreProcessingZn() {
        Element base = field.newElement().setToRandom().getImmutable();

        Element n = pairing.getZr().newElement().setToRandom();

        Element r1 = base.pow().powZn(n);
        Element r2 = base.powZn(n);

        assertTrue(r1.isEqual(r2));
    }

    @Test
    public void testPowPreProcessingBytes() {
        Element base = field.newElement().setToRandom().getImmutable();

        ElementPowPreProcessing ppp1 = base.pow();
        ElementPowPreProcessing ppp2 = base.getField().pow(ppp1.toBytes());

        Element n = pairing.getZr().newElement().setToRandom();

        Element r1 = ppp1.powZn(n);
        Element r2 = ppp2.powZn(n);

        assertTrue(r1.isEqual(r2));
    }

}
