package it.unisa.dia.gas.plaf.jpbc.util.math;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BigFraction {
    BigInteger numerator;
    BigInteger denominator;

    public BigFraction(int n) {
        numerator = BigInteger.valueOf(n);
        denominator = BigInteger.ONE;
    }

    public BigFraction(int n, int d) {
        numerator = BigInteger.valueOf(n);
        denominator = BigInteger.valueOf(d);
    }

    public BigFraction(BigInteger n, BigInteger d) {
        numerator = new BigInteger(n.toByteArray());
        denominator = new BigInteger(d.toByteArray());
    }

    public BigFraction(BigFraction f) {
        this(f.numerator, f.denominator);
    }

    public BigInteger getNominator() {
        reduceThis();
        return numerator;
    }

    public BigInteger getDenominator() {
        reduceThis();
        return denominator;
    }

    public BigFraction add(BigFraction rhs) {
        BigInteger ad = numerator.multiply(rhs.denominator);
        BigInteger bc = denominator.multiply(rhs.numerator);
        BigInteger cd = denominator.multiply(rhs.denominator);

        return new BigFraction(ad.add(bc), cd);
    }

    public BigFraction subtract(BigFraction rhs) {
        BigInteger ad = numerator.multiply(rhs.denominator);
        BigInteger bc = denominator.multiply(rhs.numerator);
        BigInteger cd = denominator.multiply(rhs.denominator);

        return new BigFraction(ad.subtract(bc), cd);
    }

    public BigFraction multiply(BigFraction rhs) {
        return new BigFraction(numerator.multiply(rhs.numerator), denominator.multiply(rhs.denominator));
    }

    public BigFraction divide(BigFraction rhs) {
        // we turn the fraction upside down and multiply
        return new BigFraction(numerator.multiply(rhs.denominator), denominator.multiply(rhs.numerator));
    }

    public BigFraction negate() {
        reduceThis();
        return new BigFraction(numerator.negate(), denominator);
    }

    public int compareTo(BigFraction rhs) {
        // There's no LCM in BigInteger, so we get identical denominators
        // by multipling them together.

        // We're not reducing since that appeared to be more
        // costly than just doing the multiplication on (potentially) larger
        // numbers.

        BigInteger lhsNominator = numerator.multiply(rhs.denominator);
        BigInteger rhsNominator = denominator.multiply(rhs.numerator);
        // the denomintor for both fractions is denom*rhs.denom, but we
        // don't need to compute that

        return lhsNominator.compareTo(rhsNominator);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BigFraction) && compareTo((BigFraction) obj) == 0;
    }

    public static String toString(BigFraction f) {
        return f.toString();
    }

    public BigFraction reduce() {
        BigInteger newNumerator, newDenominator;

        if (numerator.signum() == denominator.signum()) {
            newNumerator = numerator.abs();
            newDenominator = denominator.abs();
        } else {
            newNumerator = numerator.abs().negate();
            newDenominator = denominator.abs();
        }
        BigInteger gdc = newNumerator.gcd(newDenominator);

        if (BigInteger.ONE.equals(gdc))
            return new BigFraction(newNumerator, newDenominator);

        return new BigFraction(newNumerator.divide(gdc), newDenominator.divide(gdc)).reduce();
    }

    public void reduceThis() {
        BigFraction reduced = reduce();

        numerator = reduced.numerator;
        denominator = reduced.denominator;
    }

    public BigFraction inverse() {
        return new BigFraction(denominator, numerator);
    }

    @Override
    public String toString() {
        return String.format("BigFraction{\n\tnum=%s, \n\tdem=%s\n}", numerator, denominator);
    }
}
