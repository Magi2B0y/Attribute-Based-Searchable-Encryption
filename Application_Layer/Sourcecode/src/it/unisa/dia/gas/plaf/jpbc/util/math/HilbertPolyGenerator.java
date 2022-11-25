package it.unisa.dia.gas.plaf.jpbc.util.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;
import static java.math.RoundingMode.HALF_DOWN;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HilbertPolyGenerator {
    protected int D;

    protected BigDecimal pi, eulere, recipeulere, epsilon, negepsilon;
    protected MathContext precisionMathContext;


    public HilbertPolyGenerator(int D) {
        this.D = D;
    }

    /**
     * returns darray of mpz's that are coefficients of H_D(x)
     * (see Cohen: note my D is -D in his notation)
     *
     * @return
     */
    public BigInteger[] getHilbertPoly() {
        // Compute required precision.
        int a = 0;
        int b = D % 2;
        double d = 1.0;
        int h = 1;

        int B = (int) floor(sqrt((double) D / 3.0));
        int t = 0;
        int jcount = 1;

        boolean step1 = true, step2 = true;
        for (; ;) {
            if (step1) {
                t = (b * b + D) / 4;
                a = b;

                if (a <= 1) {
                    a = 1;
                    step2 = false;
                }
            }
            step1 = true;

            if (step2) {
                if (t % a == 0) {
                    jcount++;
                    if ((a == b) || (a * a == t) || b == 0) {
                        d += 1.0 / ((double) a);
                        h++;
                    } else {
                        d += 2.0 / ((double) a);
                        h += 2;
                    }
                }
            }
            step2 = true;

            a++;
            if (a * a <= t) {
                step1 = false;
            } else {
                b += 2;
                if (b > B)
                    break;
            }

        }

//        5.54526825320470858102580005130496645923
//        5.54526825320470858102580005130496645923

        d *= sqrt(D) * 3.14159265358979d / log(2);
        initPrecision((int) d + 34);

        System.out.println("step1 = " + new BigDecimal(sqrt(D)));
        BigDecimal sqrtD = BigDecimalUtils.sqrt(new BigDecimal(D), precisionMathContext.getPrecision())/*.setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode())*/;
//        sqrtD = new BigDecimal(sqrt(D)).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());

        System.out.println("sqrtD = " + sqrtD);
        System.out.printf("%15.80f\n", sqrt(D));
        b = D % 2;
        h = 0;
        List<Complex> Pz = new LinkedList<Complex>();
        BigDecimal f0;

        int step = 0;
        step1 = true;
        boolean running = true;

        while (running) {
            if (step1) {
                t = (b * b + D) / 4;
                if (b > 1) {
                    a = b;
                } else {
                    a = 1;
                }
            }
            step1 = false;

            if (step == 0 && t % a == 0)
                step = 2;

            switch (step) {
                case 0:
                    while (t % a != 0) {
                        a++;
                        if (a * a > t) {
                            step = 3;
                            break;
                        }
                    }
                    if (step != 3)
                        step = 2;
                    break;

                case 1:
                    do {
                        a++;
                        if (a * a > t) {
                            step = 3;
                            break;
                        }
                    } while (t % a != 0);
                    if (step != 3)
                        step = 2;

                    break;

                case 2:
                    // a, b, t/a are coeffs of an appropriate primitive reduced positive
                    // definite form.
                    // Compute j((-b + sqrt{-D})/(2a)).

                    h++;
                    System.out.printf("[%d/%d] a b c = %d %d %d\n", h, jcount, a, b, t / a);

                    f0 = BigDecimal.ONE.divide(BigDecimal.valueOf(2 * a), precisionMathContext.getPrecision() + 1, precisionMathContext.getRoundingMode());
                    System.out.println("f0 = " + f0);

                    Complex alpha = new Complex(precisionMathContext);
                    alpha.setIm(sqrtD.multiply(f0).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode()));
                    alpha.setRe(f0.multiply(BigDecimal.valueOf(b), precisionMathContext).negate(precisionMathContext));
                    System.out.println("alpha = " + alpha);

                    Complex j = computeJ(alpha);

                    System.out.println("j = " + j);

                    if (a == b || a * a == t || b == 0) {
                        // P *= X - j
                        int i, n;
                        Complex p0 = new Complex(j);
                        p0.negate();

                        n = Pz.size();
                        if (n != 0) {
                            Complex z1 = new Complex(Pz.get(0));
                            Pz.get(0).set(z1).add(p0);

                            Complex z0 = new Complex(precisionMathContext);
                            for (i = 1; i < n; i++) {
                                z0.set(z1).mul(p0);
                                z1.set(Pz.get(i));
                                Pz.get(i).add(z0);
                            }
                            p0.mul(z1);
                        }

                        Pz.add(p0);
                    } else {
                        // P *= X^2 - 2 Re(j) X + |j|^2
                        int i, n;

                        Complex p0 = new Complex(precisionMathContext), p1 = new Complex(precisionMathContext);

                        // p1 = - 2 Re(j)
                        f0 = j.getRe().multiply(BigDecimalUtils.TWO, precisionMathContext).negate(precisionMathContext);
                        p1.setRe(f0);

                        // p0 = |j|^2
                        f0 = j.getRe().multiply(j.getRe(), precisionMathContext);
                        p0.setRe(j.getIm().multiply(j.getIm(), precisionMathContext));
                        p0.setRe(p0.getRe().add(f0, precisionMathContext));

                        n = Pz.size();
                        if (n == 0) {
                        } else if (n == 1) {
                            Complex z1 = new Complex(Pz.get(0));
                            Pz.get(0).add(p1);
                            p1.mul(z1);
                            p1.add(p0);
                            p0.mul(z1);
                        } else {
                            Complex z2 = new Complex(Pz.get(0));
                            Complex z1 = new Complex(Pz.get(1));

                            Pz.get(0).set(z2).add(p1);
                            Complex z0 = new Complex(z2).mul(p1);
                            Pz.get(1).set(z1).add(z0).add(p0);

                            for (i = 2; i < n; i++) {
                                z0.set(z1).mul(p1);
                                alpha.set(z2).mul(p0);
                                z2.set(z1);
                                z1.set(Pz.get(i));
                                alpha.add(z0);
                                Pz.get(i).set(z1).add(alpha);
                            }
                            z0.set(z2).mul(p0);
                            p1.mul(z1);
                            p1.add(z0);
                            p0.mul(z1);
                        }
                        Pz.add(p1);
                        Pz.add(p0);
                    }
                    step = 1;
                    break;

                case 3:
                    b += 2;
                    if (b > B)
                        running = false;
                    step1 = true;
                    step = 0;
            }

        }

        // Round polynomial and assign.
        BigInteger[] coeff = new BigInteger[Pz.size() + 1];
        int k = 0;
        for (int i = Pz.size() - 1; i >= 0; i--) {
            f0 = Pz.get(i).getRe().signum() < 0 ? new BigDecimal("-0.5") : new BigDecimal("0.5");
            f0 = f0.add(Pz.get(i).getRe(), precisionMathContext);

            coeff[k++] = f0.toBigInteger();
            System.out.printf("coeff_%d = %s\n", k - 1, coeff[k - 1].toString());
        }
        coeff[k] = BigInteger.ONE;

        return coeff;
    }


    protected void initPrecision(int bitPrecision) {
        int decimalPrecision = BigInteger.valueOf(2).shiftLeft(bitPrecision).toString(10).length();
        precisionMathContext = new MathContext(bitPrecision, HALF_DOWN);

        // compute epsilon
        epsilon = BigDecimal.ONE.divide(BigDecimalUtils.TWO.pow(bitPrecision), decimalPrecision + 20, HALF_DOWN);
        negepsilon = epsilon.negate().setScale(precisionMathContext.getPrecision(), HALF_DOWN);

        // compute eulere
        eulere = BigDecimal.ONE;
        BigDecimal f0 = BigDecimal.ONE;
        for (int i = 1; ; i++) {
            f0 = f0.divide(BigDecimal.valueOf(i), decimalPrecision *2, HALF_DOWN);
            if (f0.compareTo(epsilon) < 0)
                break;

            System.out.println("f0 = " + f0);
            eulere = eulere.add(f0).setScale(decimalPrecision + (3*i), HALF_DOWN);
            System.out.println("eulere = " + eulere);
        }
        recipeulere = BigDecimal.ONE.divide(eulere, decimalPrecision, HALF_DOWN);

        System.out.println("epsilon = " + epsilon);
        System.out.println("eulere = " + eulere);
        System.out.println("recipeulere = " + recipeulere);

        // compute pi
        pi = BigDecimalUtils.computePI(bitPrecision);
    }


    // Computes q = exp(2 pi i tau).
    protected Complex computeQ(Complex tau) {

        // compute z0 = 2 pi i tau
        Complex z0 = new Complex(tau);

        // first remove integral part of Re(tau)
        // since exp(2 pi i)  = 1
        // it seems |Re(tau)| < 1 anyway?
        BigDecimal fp0 = z0.getRe();
        BigDecimal f1 = new BigDecimal(fp0.toBigInteger());
        fp0 = fp0.subtract(f1);

        z0.setRe(fp0);
        System.out.println("z0 = " + z0);
        System.out.println("pi = " + pi);
        z0.mul(pi);
        System.out.println("z0 = " + z0);
        z0.mul(2).muli(z0);

        // compute q = exp(z0);
        // first write z0 = A + a + b i
        // where A is a (negative) integer
        // and a, b are in [-1, 1]
        // compute e^A separately
        fp0 = z0.getRe();
        int pwr = abs(fp0.intValue());
        System.out.println("pwr = " + pwr);
        BigDecimal f0 = recipeulere.pow(pwr).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
        fp0 = fp0.add(BigDecimal.valueOf(pwr)).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
        System.out.println("fp0 = " + fp0);

        z0.setRe(fp0);
        f0 = exp(z0.getRe()).multiply(f0).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());

        Complex res = cis(z0.getIm());
        res.mul(f0);
        return res;
    }

    protected Complex cis(BigDecimal theta) {
        // out = exp(i theta)
        //     = cos theta + i sin theta
        // converges quickly near the origin
        BigDecimal temp = theta;

        BigDecimal rx = BigDecimal.ONE;
        BigDecimal ry = theta;

        int i = 1;
        boolean toggle = true;
        for (; ;) {
            toggle = !toggle;

            // Update real part
            i++;
            temp = temp.divide(BigDecimal.valueOf(i), precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode()).multiply(theta).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
            if (toggle) {
                rx = rx.add(temp).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
            } else {
                rx = rx.subtract(temp).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
            }

            // Update imaginary part
            i++;
            temp = temp.divide(BigDecimal.valueOf(i), precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode()).multiply(theta).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
            if (toggle) {
                ry = ry.add(temp).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
            } else {
                ry = ry.subtract(temp).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
            }

            // Check exit condition
            if (temp.signum() > 0) {
                if (temp.compareTo(epsilon) < 0) break;
            } else {
                if (temp.compareTo(negepsilon) > 0) break;
            }
        }

        return new Complex(precisionMathContext, rx, ry);
    }

    public BigDecimal exp(BigDecimal pwr) {
        BigDecimal res = pwr.add(BigDecimal.ONE).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());

        BigDecimal temp = pwr;
        for (int i = 2; ; i++) {
            temp = temp.multiply(pwr).divide(BigDecimal.valueOf(i), precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
            if (temp.signum() > 0) {
                if (temp.compareTo(epsilon) < 0) break;
            } else {
                if (temp.compareTo(negepsilon) > 0) break;
            }

            res = res.add(temp).setScale(precisionMathContext.getPrecision(), precisionMathContext.getRoundingMode());
        }

        return res;
    }

    /**
     * Computes z = Delta(q) (see Cohen).
     *
     * @param q
     */
    protected Complex computeDelta(Complex q) {
        Complex z0 = new Complex(precisionMathContext);
        Complex z1 = new Complex(precisionMathContext);
        Complex z2 = new Complex(precisionMathContext);

        z0.set(1);
        int d = -1;
        for (int n = 1; n < 100; n++) {
            int power = n * (3 * n - 1) / 2;
            z1.add(z2.set(q).pow(n).mul(z1.set(q).pow(power)));

            if (d != 0) {
                z0.sub(z1);
                d = 0;
            } else {
                z0.add(z1);
                d = 1;
            }
        }

        return new Complex(z0.pow(24)).mul(q);
    }

    /**
     * // Computes z = h(tau)
     * // (called h() by Blake et al, f() by Cohen.)
     *
     * @param tau
     * @return
     */
    protected Complex computeH(Complex tau) {
        Complex q = computeQ(tau);
        System.out.println("q = " + q);
        Complex z0 = computeDelta(new Complex(q).mul(q));
        Complex z1 = computeDelta(q);

        return new Complex(z0).div(z1);
    }

    /**
     * // Computes j = j(tau).
     *
     * @param tau
     * @return
     */
    protected Complex computeJ(Complex tau) {
        Complex h = computeH(tau);
        System.out.println("h = " + h);
        return new Complex(h).mul_2exp(8).add(1).pow(3).div(h);
    }


    public static void main(String[] args) {
        System.out.println(BigInteger.valueOf(2).shiftLeft(92).toString(10).length());

        HilbertPolyGenerator hilbertPolyGenerator = new HilbertPolyGenerator(59);
        hilbertPolyGenerator.getHilbertPoly();
    }
}
