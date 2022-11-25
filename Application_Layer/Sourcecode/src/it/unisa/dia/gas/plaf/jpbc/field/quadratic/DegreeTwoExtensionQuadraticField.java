package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Field;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class DegreeTwoExtensionQuadraticField<F extends Field> extends QuadraticField<F, DegreeTwoExtensionQuadraticElement> implements Serializable {

    private static final long serialVersionUID = -1L;

    public DegreeTwoExtensionQuadraticField(Random random, F targetField) {
        super(random, targetField);
    }


    public DegreeTwoExtensionQuadraticElement newElement() {
        return new DegreeTwoExtensionQuadraticElement(this);
    }

}
