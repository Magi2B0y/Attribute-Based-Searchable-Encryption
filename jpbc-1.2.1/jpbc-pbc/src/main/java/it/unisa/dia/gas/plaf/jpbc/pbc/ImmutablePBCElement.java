package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutablePBCElement extends PBCElement {

    public ImmutablePBCElement(PBCElement pbcElement) {
        super(pbcElement);
        
        this.immutable = true;
    }
    
    
    @Override
    public PBCElement set(Element value) {
        return duplicate().set(value);    
    }

    @Override
    public PBCElement set(int value) {
        return duplicate().set(value);    
    }

    @Override
    public PBCElement set(BigInteger value) {
        return duplicate().set(value);    
    }

    @Override
    public PBCElement twice() {
        return duplicate().twice();    
    }

    @Override
    public PBCElement mul(int z) {
        return duplicate().mul(z);    
    }

    @Override
    public PBCElement setToZero() {
        return duplicate().setToZero();    
    }

    @Override
    public PBCElement setToOne() {
        return duplicate().setToOne();    
    }

    @Override
    public PBCElement setToRandom() {
        return duplicate().setToRandom();    
    }

    @Override
    public PBCElement setFromHash(byte[] source, int offset, int length) {
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
    public PBCElement square() {
        return duplicate().square();    
    }

    @Override
    public PBCElement invert() {
        return duplicate().invert();    
    }

    @Override
    public PBCElement halve() {
        return duplicate().halve();    
    }

    @Override
    public PBCElement negate() {
        return duplicate().negate();    
    }

    @Override
    public PBCElement add(Element element) {
        return duplicate().add(element);    
    }

    @Override
    public PBCElement sub(Element element) {
        return duplicate().sub(element);    
    }

    @Override
    public PBCElement div(Element element) {
        return duplicate().div(element);    
    }

    @Override
    public PBCElement mul(Element element) {
        return duplicate().mul(element);    
    }

    @Override
    public PBCElement mul(BigInteger n) {
        return duplicate().mul(n);    
    }

    @Override
    public PBCElement mulZn(Element z) {
        return duplicate().mulZn(z);    
    }

    @Override
    public PBCElement sqrt() {
        return duplicate().sqrt();    
    }

    @Override
    public PBCElement pow(BigInteger n) {
        return duplicate().pow(n);    
    }

    @Override
    public PBCElement powZn(Element n) {
        return duplicate().powZn(n);    
    }
    
}
