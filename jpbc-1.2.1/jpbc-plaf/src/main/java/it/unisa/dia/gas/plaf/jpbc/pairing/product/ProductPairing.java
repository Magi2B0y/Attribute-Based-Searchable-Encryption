package it.unisa.dia.gas.plaf.jpbc.pairing.product;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingPreProcessing;

import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ProductPairing implements Pairing {
    protected Pairing basePairing;
    protected int n;

    protected Field G1, G2;

    public ProductPairing(Random random, Pairing basePairing, int n) {
        this.basePairing = basePairing;

        this.G1 = new VectorField(random, basePairing.getG1(), n);
        this.G2 = new VectorField(random, basePairing.getG2(), n);
    }


    public boolean isSymmetric() {
        return basePairing.isSymmetric();
    }

    public Field getG1() {
        return G1;
    }

    public Field getG2() {
        return G2;
    }

    public Field getGT() {
        return basePairing.getGT();
    }

    public Field getZr() {
        return basePairing.getZr();
    }

    public Element pairing(Element in1, Element in2) {
        Vector v1 = (Vector) in1;
        Vector v2 = (Vector) in2;

//      TODO:  return basePairing.pairing(v1.toArray(), v2.toArray());
        Element output = basePairing.pairing(v1.getAt(0), v2.getAt(0));
        for (int i = 1; i < v1.getSize(); i++) {
            output.mul(basePairing.pairing(v1.getAt(i), v2.getAt(i)));
        }
        return output;
    }

    public Element pairing(Element[] in1, Element[] in2) {
        Element out = pairing(in1[0], in2[0]);

        for(int i = 1; i < in1.length; i++)
            out.mul(pairing(in1[i], in2[i]));

        return out;
    }

    public PairingPreProcessing pairing(final Element in1) {
        return new AbstractMillerPairingPreProcessing() {
            public Element pairing(Element in2) {
                return ProductPairing.this.pairing(in1, in2);
            }
        };
    }

    public int getPairingPreProcessingLengthInBytes() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public PairingPreProcessing pairing(byte[] source) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public PairingPreProcessing pairing(byte[] source, int offset) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public PairingFieldIdentifier getPairingFieldIdentifier(Field field) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

}