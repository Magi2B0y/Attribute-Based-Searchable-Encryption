package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractElement;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class AbstractZElement extends AbstractElement {

    public BigInteger value;

    protected AbstractZElement(Field field) {
        super(field);
    }
}
