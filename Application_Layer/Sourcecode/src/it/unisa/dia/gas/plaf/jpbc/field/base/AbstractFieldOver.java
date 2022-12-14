package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.FieldOver;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class AbstractFieldOver<F extends Field, E extends Element> extends AbstractField<E> implements FieldOver<F, E>, Serializable {

    private static final long serialVersionUID = -1L;

    protected F targetField;


    protected AbstractFieldOver(Random random, F targetField) {
        super(random);
        this.targetField = targetField;
    }


    public F getTargetField() {
        return targetField;
    }

}
