package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import it.unisa.dia.gas.plaf.jpbc.util.math.HilbertPolyGenerator;
import it.unisa.dia.gas.plaf.jpbc.util.math.PellEquation;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDCurveGenerator implements CurveGenerator {

    protected int discriminant;

    protected DefaultCurveParameters[] curves;
    protected BigInteger D3;
    protected int bitLimit;

    protected Random random;

    public TypeDCurveGenerator(int discriminant, Random random) {
        this();
        this.discriminant = discriminant;
        this.random = random;
    }

    public TypeDCurveGenerator(int discriminant) {
        this();
        this.random = new SecureRandom();
        setDiscriminant(discriminant);
    }

    public TypeDCurveGenerator() {
        this.random = new SecureRandom();
        this.bitLimit = 500;
    }




    public CurveParameters generate() {
        if (curves == null || curves.length == 0)
            throw new IllegalStateException("Cannot find valid curves. Try another discriminant.");

        for (DefaultCurveParameters curve : curves) {
            d_param_from_cm(curve);
        }

        return curves[0];
    }


    public int getDiscriminant() {
        return discriminant;
    }

    public void setDiscriminant(int discriminant) {
        if (discriminant < 7 || (discriminant % 4) != 3) {
            throw new IllegalArgumentException("D must be 3 mod 4 and at least 7.");
        }

        BigInteger D3 = BigInteger.valueOf(discriminant * 3l);
        if (BigIntegerUtils.isPerfectSquare(D3)) {
            // (the only squares that differ by 8 are 1 and 9,
            // which we get if U=V=1, D=3, but then l is not an integer)
            throw new IllegalArgumentException("Invalid discriminant. 3*D is a perfect square.");
        }

        this.discriminant = discriminant;
        this.D3 = D3;

        // Generate curves
        curves = findCurves();
        if (curves == null || curves.length == 0)
            throw new IllegalArgumentException("Cannot find valid curves. Try another discriminant.");
    }

    /**
     * Finds the next valid discriminant starting from the current discriminant value.
     */
    public void nextDiscriminant() {
        int current = this.discriminant;

        while (true) {
            try {
                setDiscriminant(++current);
                return;
            } catch (Exception e) {
            }
        }
    }

    /**
     * Finds all the feasible curve for the current discriminant.
     *
     * @return all the feasible curve for the current discriminant.
     */
    protected DefaultCurveParameters[] findCurves() {
        List<DefaultCurveParameters> curves = new ArrayList<DefaultCurveParameters>();

        BigInteger t0, t1, t2;

        PellEquation pellEquation = new PellEquation(D3, -8);
        pellEquation.solve();
        int n = pellEquation.count;

        if (n != 0) {
            boolean found = false;

            // Copy pell equation solution
            BigInteger[] x = pellEquation.x.clone();
            BigInteger[] y = pellEquation.y.clone();

            while (!found) {

                for (int i = 0; i < n; i++) {
                    //element_printf("%Zd, %Zd\n", ps->x[i], ps->y[i]);

                    DefaultCurveParameters params = doMNTStep(x[i]);
                    if (params != null)
                        curves.add(params);

                    // compute next solution as follows
                    // if p, q is current solution
                    // compute new solution p', q' via
                    // (p + q sqrt{3D})(t + u sqrt{3D}) = p' + q' sqrt(3D)
                    // where t, u is min. solution to Pell equation

                    t0 = pellEquation.minx.multiply(x[i]);
                    t1 = pellEquation.miny.multiply(y[i]);
                    t1 = t1.multiply(D3);
                    t0 = t0.add(t1);

                    if (2 * t0.bitLength() > bitLimit + 10) {
                        found = true;
                        break;
                    }

                    t2 = pellEquation.minx.multiply(y[i]);
                    t1 = pellEquation.miny.multiply(x[i]);
                    t2 = t2.add(t1);

                    x[i] = t0;
                    y[i] = t2;
                }

            }
        }

        return curves.toArray(new DefaultCurveParameters[curves.size()]);
    }

    protected DefaultCurveParameters doMNTStep(BigInteger U) {
        DefaultCurveParameters params = new DefaultCurveParameters();

        // Compute l and d
        int d;
        BigInteger l = U.mod(BigIntegerUtils.SIX);
        if (l.compareTo(BigInteger.ONE) == 0) {
            l = U.subtract(BigInteger.ONE);
            d = 1;
        } else if (l.compareTo(BigIntegerUtils.FIVE) == 0) {
            l = U.add(BigInteger.ONE);
            d = -1;
        } else {
            return null;
        }
        l = l.divide(BigIntegerUtils.THREE);

        // Compute q = l^2 + 1
        BigInteger q = l.multiply(l).add(BigInteger.ONE);
        if (!q.isProbablePrime(10)) {
            return null;
        }

        // Compute n
        BigInteger n = d < 0 ? q.subtract(l) : q.add(l);

        // Compute the final things...
        BigInteger cofac = BigInteger.ONE;
        BigInteger r = n;
        BigInteger p = BigIntegerUtils.TWO;

        if (!r.isProbablePrime(10)) {
            while (true) {
                if (BigIntegerUtils.isDivisible(r, p)) {
                    do {
                        cofac = cofac.multiply(p);
                        r = r.divide(p);
                    } while (BigIntegerUtils.isDivisible(r, p));
                }

                if (r.isProbablePrime(10))
                    break;

                //TODO: use a table of primes instead?
                p = p.nextProbablePrime();
                if (p.bitLength() > 16) {
                    // printf("has 16+ bit factor\n");
                    return null;
                }
            }
        }

        params.put("type", "d");
        params.put("k", "6");
        params.put("D", String.valueOf(discriminant));
        params.put("q", q.toString());
        params.put("r", r.toString());
        params.put("h", cofac.toString());
        params.put("n", n.toString());

        return params;
    }

    protected void d_param_from_cm(DefaultCurveParameters param) {
        compute_cm_curve(param);

        Field<? extends Element> Fq = new ZrField(random, param.getBigInteger("q"));
        PolyField Fqx = new PolyField<Field>(random, Fq);

        PolyElement irred = Fqx.newElement();
        do {
            irred.setToRandomMonic(3);
        } while (!irred.isIrriducible());

        PolyModField Fqd = new PolyModField<Field>(random, irred, null);

        // find a quadratic nonresidue of Fqd lying in Fq
        PolyModElement nqr = Fqd.newElement();
        do {
            nqr.getCoefficient(0).setToRandom();
        } while (nqr.isSqr());

        int d = 3;
        for (int i = 0; i < d; i++) {
            param.put("coeff" + i, irred.getCoefficient(i).toBigInteger().toString());
        }
        param.put("nqr", nqr.getCoefficient(0).toBigInteger().toString());
    }

    /**
     * computes a curve and sets fp to the field it is defined over
     * using the complex multiplication method, where cm holds
     * the appropriate information (e.g. discriminant, field order)
     *
     * @param param
     */
    protected void compute_cm_curve(DefaultCurveParameters param) {
        ZrField fp = new ZrField(random,param.getBigInteger("q"));
        PolyField fpx = new PolyField(random, fp);

        // Init Hilbert Poly Element and find a root
        PolyElement hp = fpx.newElement().setFromCoefficientMonic(new HilbertPolyGenerator(param.getInt("D")).getHilbertPoly());
        Element root = hp.findRoot();
        if (root == null)
            throw new IllegalStateException("No root for hilbert polynomial");

        // The root is the j-invariant of the desired curve.
        CurveField cc = CurveField.newCurveFieldJ(new SecureRandom(), root, param.getBigInteger("n"), null);

        // We may need to twist it.
        // Pick a random point P and twist the curve if it has the wrong order.
        Element P = cc.newRandomElement().mul(param.getBigInteger("n"));
        if (!P.isZero())
            cc.twist();

        param.put("a", cc.getA().toBigInteger().toString());
        param.put("b", cc.getB().toBigInteger().toString());

        {
            // Compute order of curve in F_q^k.
            // n = q - t + 1 hence t = q - n + 1
            BigInteger z = param.getBigInteger("q").subtract(param.getBigInteger("n")).add(BigInteger.ONE);
            z = BigIntegerUtils.traceN(param.getBigInteger("q"),
                    z,
                    param.getInt("k"));

            BigInteger nk = param.getBigInteger("q").pow(param.getInt("k"));
            z = z.subtract(BigInteger.ONE);
            nk = nk.subtract(z);
            z = param.getBigInteger("r").multiply(param.getBigInteger("r"));

            BigInteger hk = nk.divide(z);

            param.put("nk", nk.toString());
            param.put("hk", hk.toString());
        }
    }


    public static void main(String[] args) {
/*
        TypeDCurveGenerator generator = new TypeDCurveGenerator(59);
        while (generator.getDiscriminant() < 2000) {
            generator.generate();

            for (Map<String, String> curve : generator.curves) {
                System.out.printf("%s, %d, %d\n",
                                  curve.get("D"),
                                  new BigInteger(curve.get("q")).bitLength(),
                                  new BigInteger(curve.get("r")).bitLength()
                );
            }

            generator.nextDiscriminant();
        }
*/
        TypeDCurveGenerator generator = new TypeDCurveGenerator(9563);
        DefaultCurveParameters params = (DefaultCurveParameters) generator.generate();
        System.out.println(params.toString());
    }

}
