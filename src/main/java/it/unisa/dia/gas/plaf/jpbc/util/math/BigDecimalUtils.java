package it.unisa.dia.gas.plaf.jpbc.util.math;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.RoundingMode.HALF_DOWN;
import static java.math.RoundingMode.HALF_UP;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BigDecimalUtils {
    public static final BigDecimal TWO = BigDecimal.valueOf(2);

    public static BigDecimal computePI(int precision) {
        int oldPrecision = precision;
        precision = 101;

        BigInteger k1 = new BigInteger("545140134");
        BigInteger k2 = new BigInteger("13591409");
        BigInteger k4 = new BigInteger("100100025");
        BigInteger k5 = new BigInteger("327843840");

        int k3 = 640320;
        int k6 = 53360;

        BigInteger d = k4.multiply(k5);
        d = d.multiply(BigIntegerUtils.EIGHT);
        BigFraction p = new BigFraction(BigInteger.ZERO, BigInteger.ONE);
        BigFraction q;

        int nLimit = precision / 47 + 1;
        boolean toggle = true;
        for (int n = 0; n < nLimit; n++) {
            BigInteger z0 = BigIntegerUtils.factorial(6 * n);
            BigInteger z1 = k1.multiply(BigInteger.valueOf(n));
            z1 = z1.add(k2);
            z0 = z0.multiply(z1);

            z1 = BigIntegerUtils.factorial(3 * n);
            BigInteger z2 = BigIntegerUtils.factorial(n);
            z2 = z2.pow(3);
            z1 = z1.multiply(z2);                                                    
            z2 = d.pow(n);
            z1 = z1.multiply(z2);

            q = new BigFraction(z0, z1);
            System.out.println("q = " + q);
            q.reduceThis();
            System.out.println("CAN q = " + q);

            if (toggle) {
                p = p.add(q);
            } else {
                p = p.subtract(q);
            }
            toggle = !toggle;
        }
        p.reduceThis();

        System.out.println("p = " + p);
        q = p.inverse();
        q = new BigFraction(
                q.getNominator().multiply(BigInteger.valueOf(k6)),
                q.getDenominator()
        );
        q.reduceThis();
        System.out.println("q = " + q);

//        3.14159265358979323846264338327950288420
//        3.141592653589793238462643383279502884197
        
        BigDecimal pi = new BigDecimal(q.getNominator());
        pi = pi.divide(new BigDecimal(q.getDenominator()), oldPrecision + 4, HALF_DOWN);
        System.out.println("pi = " + pi);
        BigDecimal f1 = BigDecimalUtils.sqrt(new BigDecimal(k3), oldPrecision);

        pi = pi.multiply(f1).setScale(oldPrecision + 1, HALF_DOWN);
        System.out.println("pi = " + pi);

//        System.exit(0);

        return pi;
    }

    /**
     * the Babylonian square root method (Newton's method)
     *
     * @param A
     * @param SCALE
     * @return
     */
    public static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, HALF_UP);
        }

        return x1;
    }

}
