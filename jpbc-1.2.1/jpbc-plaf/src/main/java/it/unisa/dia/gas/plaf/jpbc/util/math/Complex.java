package it.unisa.dia.gas.plaf.jpbc.util.math;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class Complex {
    protected MathContext mathContext;
    protected BigDecimal re, im;


    public Complex(MathContext mathContext) {
        this.mathContext = mathContext;

        this.re = BigDecimal.ZERO;
        this.im = this.re;
    }

    public Complex(MathContext mathContext, BigDecimal re, BigDecimal im) {
        this.mathContext = mathContext;

        this.re = re;
        this.im = im;
    }

    public Complex(Complex complex) {
        this.mathContext = complex.mathContext;

        this.re = complex.re;
        this.im = complex.im;
    }


    public BigDecimal getRe() {
        return re;
    }

    public void setRe(BigDecimal re) {
        this.re = re;
    }

    public BigDecimal getIm() {
        return im;
    }

    public void setIm(BigDecimal im) {
        this.im = im;
    }

    public Complex negate() {
        re = re.negate().setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        im = im.negate().setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        return this;
    }

    public Complex set(Complex complex) {
        this.mathContext = complex.mathContext;

        this.re = complex.re;
        this.im = complex.im;

        return this;
    }

    public Complex add(Complex value) {
        this.re = this.re.add(value.re).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        this.im = this.im.add(value.im).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        return this;
    }

    public Complex mul(Complex value) {
        BigDecimal ac = this.re.multiply(value.re).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        BigDecimal bd = this.im.multiply(value.im).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        BigDecimal f0 = this.re.add(this.im).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        this.im = value.re.add(value.im)
                .multiply(f0)
                .subtract(ac)
                .subtract(bd).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        this.re = ac.subtract(bd).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        return this;
    }

    public Complex mul_2exp(int value) {
        // Compute 2^value
        BigDecimal pow = BigDecimalUtils.TWO.pow(value).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        this.re = this.re.multiply(pow).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        this.im = this.im.multiply(pow).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        return this;
    }

    public Complex mul(BigDecimal value) {
        this.re = this.re.multiply(value).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        this.im = this.im.multiply(value).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        return this;
    }

    public Complex mul(int value) {
        return mul(BigDecimal.valueOf(value));
    }

    public Complex muli(Complex value) {
        //i(a+bi) = -b + ai
        BigDecimal f0 = value.im.negate().setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        im = re;
        re = f0;

        return this;
    }

    public Complex set(int value) {
        this.re = BigDecimal.valueOf(value);
        this.im = BigDecimal.ZERO;

        return this;
    }

    public Complex pow(int power) {
        if (power < 0)
            throw new IllegalArgumentException("power must be greater than or equal zero.");

        // set m to biggest power of 2 less than n
        int m = 1;
        while (m <= power) {
            m <<= 1;
        }
        m >>= 1;

        Complex z0 = new Complex(mathContext).set(1);

        while (m != 0) {
            z0.mul(z0);
            if ((m & power) != 0) {
               z0.mul(this);
            }
            m >>= 1;
        }

        set(z0);
        return this;
    }

    public Complex sub(Complex value) {
        this.re = this.re.subtract(value.re).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        this.im = this.im.subtract(value.im).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        return this;
    }

    public Complex div(Complex value) {
        mul(new Complex(value).invert());

        return this;
    }

    private Complex invert() {
        //1/(a+bi) = (1/(a^2 + b^2))(a-bi)
        //z. TODO: use one that is less prone to (over/under)flows/precision loss
        BigDecimal f0 = re.multiply(re).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        BigDecimal f1 = im.multiply(im).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        f0 = f0.add(f1).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        f0 = BigDecimal.ONE.divide(f0, mathContext.getPrecision(), mathContext.getRoundingMode());

        re = re.multiply(f0).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        f0 = f0.negate().setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
        im = im.multiply(f0).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        return this;
    }


    public Complex add(int value) {
        this.re = this.re.add(BigDecimal.valueOf(value)).setScale(mathContext.getPrecision(), mathContext.getRoundingMode());

        return this;
    }


    @Override
    public String toString() {
        return String.format("Complex{\n\tim=%s, \n\tre=%s\n}", im, re);
    }
}
