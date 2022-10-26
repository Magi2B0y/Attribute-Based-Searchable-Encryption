package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.FieldOver;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.jpbc.Vector;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class AbstractPointElement<E extends Element> extends AbstractElement implements Point<E>, Vector<E> {

    protected AbstractFieldOver field;
    protected E x, y;


    protected AbstractPointElement(FieldOver field) {
        super(field);
        
        this.field = (AbstractFieldOver) field;
    }


    public int getSize() {
        return 2;
    }

    public E getAt(int index) {
        return (index == 0) ? x : y;
    }

    public E getX() {
        return x;
    }

    public E getY() {
        return y;
    }


    public int getLengthInBytesCompressed() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] toBytesCompressed() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setFromBytesCompressed(byte[] source) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setFromBytesCompressed(byte[] source, int offset) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getLengthInBytesX() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] toBytesX() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setFromBytesX(byte[] source) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setFromBytesX(byte[] source, int offset) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

}
