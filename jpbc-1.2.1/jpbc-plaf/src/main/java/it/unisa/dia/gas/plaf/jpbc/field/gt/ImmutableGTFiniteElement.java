package it.unisa.dia.gas.plaf.jpbc.field.gt;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutableGTFiniteElement extends GTFiniteElement {

    public ImmutableGTFiniteElement(GTFiniteElement gtFiniteElement) {
        super(gtFiniteElement);
        
        this.immutable = true;
    }

    @Override
    public GTFiniteElement set(Element value) {
        return duplicate().set(value);    
    }

    @Override
    public GTFiniteElement set(int value) {
        return duplicate().set(value);    
    }

    @Override
    public GTFiniteElement set(BigInteger value) {
        return duplicate().set(value);    
    }

    @Override
    public GTFiniteElement twice() {
        return (GTFiniteElement) duplicate().twice();
    }

    @Override
    public GTFiniteElement mul(int z) {
        return (GTFiniteElement) duplicate().mul(z);
    }

    @Override
    public GTFiniteElement setToZero() {
        return duplicate().setToZero();    
    }

    @Override
    public GTFiniteElement setToOne() {
        return duplicate().setToOne();    
    }

    @Override
    public GTFiniteElement setToRandom() {
        return duplicate().setToRandom();    
    }

    @Override
    public GTFiniteElement setFromHash(byte[] source, int offset, int length) {
        return duplicate().setFromHash(source, offset, length);    
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
    public GTFiniteElement square() {
        return (GTFiniteElement) duplicate().square();
    }

    @Override
    public GTFiniteElement invert() {
        return duplicate().invert();    
    }

    @Override
    public GTFiniteElement halve() {
        return (GTFiniteElement) duplicate().halve();
    }

    @Override
    public GTFiniteElement negate() {
        return duplicate().negate();    
    }

    @Override
    public GTFiniteElement add(Element element) {
        return duplicate().add(element);    
    }

    @Override
    public GTFiniteElement sub(Element element) {
        return duplicate().sub(element);    
    }

    @Override
    public GTFiniteElement div(Element element) {
        return duplicate().div(element);    
    }

    @Override
    public GTFiniteElement mul(Element element) {
        return duplicate().mul(element);    
    }

    @Override
    public GTFiniteElement mul(BigInteger n) {
        return duplicate().mul(n);    
    }

    @Override
    public GTFiniteElement mulZn(Element z) {
        return (GTFiniteElement) duplicate().mulZn(z);
    }

    @Override
    public GTFiniteElement sqrt() {
        return (GTFiniteElement) duplicate().sqrt();    
    }

    @Override
    public GTFiniteElement pow(BigInteger n) {
        return duplicate().pow(n);    
    }

    @Override
    public GTFiniteElement powZn(Element n) {
        return duplicate().powZn(n);    
    }

}