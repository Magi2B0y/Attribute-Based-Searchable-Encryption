package it.unisa.dia.gas.plaf.jpbc.pairing.g;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import it.unisa.dia.gas.plaf.jpbc.util.math.PellEquation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeGCurveGenerator implements CurveGenerator {

    protected int discriminant;

    protected DefaultCurveParameters[] curves;
    protected BigInteger D15;
    protected int bitLimit;


    public TypeGCurveGenerator(int discriminant) {
        this();
        setDiscriminant(discriminant);
    }

    public TypeGCurveGenerator() {
        this.bitLimit = 500;
    }


    public CurveParameters generate() {
        if (curves == null || curves.length == 0)
            throw new IllegalStateException("Cannot find valid curves. Try another discriminant.");

        for (DefaultCurveParameters curve : curves) {
            pbc_param_init_g_gen(curve);
        }

        return curves[0];
    }


    public int getDiscriminant() {
        return discriminant;
    }

    public void setDiscriminant(int discriminant) {
        int m = discriminant % 120;
        if (discriminant <= 0 || (m != 43 && m != 67)) {
            throw new IllegalArgumentException("D must be 43 or 67 mod 120 and positive.");
        }

        BigInteger D15 = BigInteger.valueOf(discriminant * 15l);
        if (BigIntegerUtils.isPerfectSquare(D15)) {
            // (the only squares that differ by 8 are 1 and 9,
            // which we get if U=V=1, D=3, but then l is not an integer)
            throw new IllegalArgumentException("Invalid discriminant. 15*D is a perfect square.");
        }

        this.discriminant = discriminant;
        this.D15 = D15;

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

        PellEquation pellEquation = new PellEquation(D15, -20);
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

                    DefaultCurveParameters params = freemanStep2(x[i]);
                    if (params != null)
                        curves.add(params);

                    // compute next solution as follows
                    // if p, q is current solution
                    // compute new solution p', q' via
                    //   (p + q sqrt{15D})(t + u sqrt{15D}) = p' + q' sqrt(15D)
                    // where t, u is min. solution to Pell equation

                    t0 = pellEquation.minx.multiply(x[i]);
                    t1 = pellEquation.miny.multiply(y[i]);
                    t1 = t1.multiply(D15);
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

    protected DefaultCurveParameters freemanStep2(BigInteger U) {
        DefaultCurveParameters params = new DefaultCurveParameters();

        BigInteger x = U.mod(BigInteger.valueOf(15));

        if (!x.equals(BigInteger.valueOf(5))) {
            x = U.subtract(BigInteger.valueOf(5));
        } else if (!x.equals(BigInteger.valueOf(10))) {
            x = U.add(BigInteger.valueOf(5));
        } else {
            return null;
        }

        x = x.divide(BigInteger.valueOf(15));

        //q = 25x^4 + 25x^3 + 25x^2 + 10x + 3
        BigInteger r = x.multiply(x);
        BigInteger q = x.add(x);
        r = r.multiply(BigInteger.valueOf(5));
        q = q.add(r);
        r = r.multiply(x);
        q = q.add(r);
        r = r.multiply(x);
        q = q.add(r);
        q = q.multiply(BigInteger.valueOf(5));
        q = q.add(BigInteger.valueOf(3));

        if (!q.isProbablePrime(10)) {
            return null;
        }

        //t = 10x^2 + 5x + 3
        //n = q - t + 1
        BigInteger n = x.multiply(BigInteger.valueOf(5));
        r = n.multiply(x);
        r = r.add(r);
        n = n.add(r);
        n = q.subtract(n);
        n = n.subtract(BigInteger.valueOf(2));

        BigInteger cofac = BigInteger.ONE;
        r = n;
        BigInteger p = BigIntegerUtils.TWO;

        if (!r.isProbablePrime(10))
            for (; ;) {

                if (BigIntegerUtils.isDivisible(r, p))
                    do {
                        cofac = cofac.multiply(p);
                        r = r.divide(p);
                    } while (BigIntegerUtils.isDivisible(r, p));

                if (r.isProbablePrime(10))
                    break;

                //TODO: use a table of primes instead?
                p = p.nextProbablePrime();
                if (p.bitLength() > 16) {
                    //printf("has 16+ bit factor\n");
                    return null;
                }
            }

        // Store parameters
        params.put("type", "g");
        params.put("k", "10");
        params.put("D", String.valueOf(discriminant));
        params.put("q", q.toString());
        params.put("r", r.toString());
        params.put("h", cofac.toString());
        params.put("n", n.toString());

        return params;
    }

    protected void pbc_param_init_g_gen(DefaultCurveParameters curveParams) {
/*        g_init(p);
        g_param_ptr param = p - > data;
        field_t Fq, Fqx, Fqd;
        element_t irred, nqr;
        int i;

        compute_cm_curve(param, cm);

        field_init_fp(Fq, param - > q);
        field_init_poly(Fqx, Fq);
        element_init(irred, Fqx);
        do {
            poly_random_monic(irred, 5);
        } while (!poly_is_irred(irred));
        field_init_polymod(Fqd, irred);

        // Find a quadratic nonresidue of Fqd lying in Fq.
        element_init(nqr, Fqd);
        do {
            element_random(((element_t *)nqr - > data)[0]);
        } while (element_is_sqr(nqr));

        param - > coefficients = pbc_realloc(param - > coefficients, sizeof(mpz_t) * 5);

        for (i = 0; i < 5; i++) {
            mpz_init(param - > coefficients[i]);
            element_to_mpz(param - > coefficients[i], element_item(irred, i));
        }
        element_to_mpz(param - > nqr, ((element_t *)nqr - > data)[0]);*/
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
        TypeGCurveGenerator generator = new TypeGCurveGenerator(9563);
        DefaultCurveParameters params = (DefaultCurveParameters) generator.generate();
        System.out.println(params.toString());
    }


}