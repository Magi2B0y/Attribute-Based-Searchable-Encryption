package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutableQuadraticElement<E extends Element> extends QuadraticElement<E> {

    public ImmutableQuadraticElement(QuadraticElement<E> element) {
        super(element.getField());

        this.x = (E) x.getImmutable();
        this.y = (E) y.getImmutable();

        this.immutable = true;
    }


    @Override
    public QuadraticElement set(Element e) {
        return duplicate().set(e);    
    }

    @Override
    public QuadraticElement set(int value) {
        return duplicate().set(value);    
    }

    @Override
    public QuadraticElement set(BigInteger value) {
        return duplicate().set(value);    
    }

    @Override
    public QuadraticElement setToZero() {
        return duplicate().setToZero();    
    }

    @Override
    public QuadraticElement setToOne() {
        return duplicate().setToOne();    
    }

    @Override
    public QuadraticElement setToRandom() {
        return duplicate().setToRandom();    
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        return duplicate().setFromBytes(source, offset);    
    }

    @Override
    public QuadraticElement twice() {
        return duplicate().twice();    
    }

    @Override
    public QuadraticElement mul(int z) {
        return duplicate().mul(z);    
    }

    @Override
    public QuadraticElement square() {
        return duplicate().square();    
    }

    @Override
    public QuadraticElement invert() {
        return duplicate().invert();    
    }

    @Override
    public QuadraticElement negate() {
        return duplicate().negate();    
    }

    @Override
    public QuadraticElement add(Element e) {
        return duplicate().add(e);    
    }

    @Override
    public QuadraticElement sub(Element e) {
        return duplicate().sub(e);    
    }

    @Override
    public QuadraticElement mul(Element e) {
        return duplicate().mul(e);    
    }

    @Override
    public QuadraticElement mul(BigInteger n) {
        return duplicate().mul(n);    
    }

    @Override
    public QuadraticElement mulZn(Element e) {
        return duplicate().mulZn(e);    
    }

    @Override
    public QuadraticElement sqrt() {
        return duplicate().sqrt();    
    }

    @Override
    public QuadraticElement powZn(Element n) {
        return duplicate().powZn(n);    
    }

    @Override
    public QuadraticElement setFromHash(byte[] source, int offset, int length) {
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
    public Element getImmutable() {
        return this;
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
    public Element div(Element element) {
        return duplicate().div(element);    
    }

}
