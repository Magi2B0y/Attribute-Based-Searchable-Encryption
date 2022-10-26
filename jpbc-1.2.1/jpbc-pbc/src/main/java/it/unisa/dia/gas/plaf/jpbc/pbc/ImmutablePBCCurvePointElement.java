package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutablePBCCurvePointElement extends PBCCurvePointElement {

    public ImmutablePBCCurvePointElement(PBCCurvePointElement pbcElement) {
        super(pbcElement);

        this.immutable = true;
    }


    @Override
    public PBCCurvePointElement set(Element value) {
        return (PBCCurvePointElement) duplicate().set(value);
    }

    @Override
    public PBCCurvePointElement set(int value) {
        return (PBCCurvePointElement) duplicate().set(value);
    }

    @Override
    public PBCCurvePointElement set(BigInteger value) {
        return (PBCCurvePointElement) duplicate().set(value);
    }

    @Override
    public PBCCurvePointElement twice() {
        return (PBCCurvePointElement) duplicate().twice();
    }

    @Override
    public PBCCurvePointElement mul(int z) {
        return (PBCCurvePointElement) duplicate().mul(z);
    }

    @Override
    public PBCCurvePointElement setToZero() {
        return (PBCCurvePointElement) duplicate().setToZero();
    }

    @Override
    public PBCCurvePointElement setToOne() {
        return (PBCCurvePointElement) duplicate().setToOne();
    }

    @Override
    public PBCCurvePointElement setToRandom() {
        return (PBCCurvePointElement) duplicate().setToRandom();
    }

    @Override
    public PBCCurvePointElement setFromHash(byte[] source, int offset, int length) {
        return (PBCCurvePointElement) duplicate().setFromHash(source, offset, length);
    }

    @Override
    public int setFromBytes(byte[] source) {
        return duplicate().setFromBytes(source);
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        return duplicate().setFromBytes(source, offset);
    }

    @Override
    public PBCCurvePointElement square() {
        return (PBCCurvePointElement) duplicate().square();
    }

    @Override
    public PBCCurvePointElement invert() {
        return (PBCCurvePointElement) duplicate().invert();
    }

    @Override
    public PBCCurvePointElement halve() {
        return (PBCCurvePointElement) duplicate().halve();
    }

    @Override
    public PBCCurvePointElement negate() {
        return (PBCCurvePointElement) duplicate().negate();
    }

    @Override
    public PBCCurvePointElement add(Element element) {
        return (PBCCurvePointElement) duplicate().add(element);
    }

    @Override
    public PBCCurvePointElement sub(Element element) {
        return (PBCCurvePointElement) duplicate().sub(element);
    }

    @Override
    public PBCCurvePointElement div(Element element) {
        return (PBCCurvePointElement) duplicate().div(element);
    }

    @Override
    public PBCCurvePointElement mul(Element element) {
        return (PBCCurvePointElement) duplicate().mul(element);
    }

    @Override
    public PBCCurvePointElement mul(BigInteger n) {
        return (PBCCurvePointElement) duplicate().mul(n);
    }

    @Override
    public PBCCurvePointElement mulZn(Element z) {
        return (PBCCurvePointElement) duplicate().mulZn(z);
    }

    @Override
    public PBCCurvePointElement sqrt() {
        return (PBCCurvePointElement) duplicate().sqrt();
    }

    @Override
    public PBCCurvePointElement pow(BigInteger n) {
        return (PBCCurvePointElement) duplicate().pow(n);
    }

    @Override
    public PBCCurvePointElement powZn(Element n) {
        return (PBCCurvePointElement) duplicate().powZn(n);
    }

}