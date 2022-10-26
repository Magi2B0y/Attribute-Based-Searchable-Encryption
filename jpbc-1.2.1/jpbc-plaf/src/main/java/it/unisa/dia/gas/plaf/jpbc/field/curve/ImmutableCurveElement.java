package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutableCurveElement<E extends Element> extends CurveElement<E> {

    public ImmutableCurveElement(CurveElement curveElement) {
        super(curveElement);
        this.x = (E) this.x.getImmutable();
        this.y = (E) this.y.getImmutable();

        this.immutable = true;
    }


    @Override
    public CurveElement set(Element e) {
        return duplicate().set(e);    
    }

    @Override
    public CurveElement set(int value) {
        return duplicate().set(value);    
    }

    @Override
    public CurveElement set(BigInteger value) {
        return duplicate().set(value);    
    }

    @Override
    public CurveElement twice() {
        return duplicate().twice();    
    }

    @Override
    public CurveElement setToZero() {
        return duplicate().setToZero();    
    }

    @Override
    public CurveElement setToOne() {
        return duplicate().setToOne();    
    }

    @Override
    public CurveElement setToRandom() {
        return duplicate().setToRandom();    
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        return duplicate().setFromBytes(source, offset);    
    }

    @Override
    public CurveElement square() {
        return duplicate().square();    
    }

    @Override
    public CurveElement invert() {
        return duplicate().invert();    
    }

    @Override
    public CurveElement negate() {
        return duplicate().negate();    
    }

    @Override
    public CurveElement add(Element e) {
        return duplicate().add(e);    
    }

    @Override
    public CurveElement mul(Element e) {
        return duplicate().mul(e);    
    }

    @Override
    public CurveElement mul(BigInteger n) {
        return duplicate().mul(n);    
    }

    @Override
    public CurveElement mulZn(Element e) {
        return duplicate().mulZn(e);    
    }

    @Override
    public CurveElement powZn(Element e) {
        return duplicate().powZn(e);    
    }

    @Override
    public CurveElement setFromHash(byte[] source, int offset, int length) {
        return duplicate().setFromHash(source, offset, length);    
    }

    @Override
    public int setFromBytesCompressed(byte[] source) {
        return duplicate().setFromBytesCompressed(source);    
    }

    @Override
    public int setFromBytesCompressed(byte[] source, int offset) {
        return duplicate().setFromBytesCompressed(source, offset);    
    }

    @Override
    public int setFromBytesX(byte[] source) {
        return duplicate().setFromBytesX(source);    
    }

    @Override
    public int setFromBytesX(byte[] source, int offset) {
        return duplicate().setFromBytesX(source, offset);    
    }

    @Override
    public int setFromBytes(byte[] source) {
        return duplicate().setFromBytes(source);    
    }

    @Override
    public Element pow(BigInteger n) {
        return duplicate().pow(n);    
    }

    @Override
    public Element halve() {
        return duplicate().halve();    
    }

    @Override
    public Element sub(Element element) {
        return duplicate().sub(element);    
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element);    
    }

    @Override
    public Element mul(int z) {
        return duplicate().mul(z);    
    }

    @Override
    public Element sqrt() {
        return duplicate().sqrt();    
    }

}
