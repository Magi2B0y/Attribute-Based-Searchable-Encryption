package it.unisa.dia.gas.plaf.jpbc.field.gt;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.PairingMap;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class GTFiniteField<F extends Field> extends AbstractFieldOver<F, GTFiniteElement> {
    protected PairingMap pairing;
    protected BigInteger order;


    public GTFiniteField(Random random, BigInteger order, PairingMap pairing, Field targetField) {
        super(random, (F) targetField);

        this.order = order;
        this.pairing = pairing;
    }

    
    public GTFiniteElement newElement() {
        return new GTFiniteElement(pairing, this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public GTFiniteElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getLengthInBytes() {
        return getTargetField().getLengthInBytes();
    }

}
