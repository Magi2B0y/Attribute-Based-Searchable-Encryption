package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Pointer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCPairingType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class PBCField extends AbstractField {
    protected PBCPairingType pairing;

    protected int fixedLengthInBytes;
    protected BigInteger order;


    protected PBCField(PBCPairingType pairing) {
        super(null);

        this.pairing = pairing;

        if (pairing != null) {
            PBCElement temp = (PBCElement) newElement();

            this.fixedLengthInBytes = WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(temp.value);

            MPZElementType mpzOrder = new MPZElementType();
            mpzOrder.init();
            WrapperLibraryProvider.getWrapperLibrary().pbc_field_order(temp.value, mpzOrder);
            this.order = new BigInteger(mpzOrder.toString(10));
        }
    }


    public BigInteger getOrder() {
        return order;
    }

    public boolean isOrderOdd() {
        return BigIntegerUtils.isOdd(order);
    }

    public Element getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

    public Element[] twice(Element[] elements) {
        Pointer[] pointers = new Pointer[elements.length];
        for (int i = 0; i < elements.length; i++) {
            pointers[i] = ((PBCElement) elements[i]).getValue();
        }

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_multi_double(pointers, pointers, elements.length);

        return elements;
    }

    public Element[] add(Element[] a, Element[] b) {
        Pointer[] aPointers = new Pointer[a.length];
        for (int i = 0; i <a.length; i++) {
            aPointers[i] = ((PBCElement) a[i]).getValue();
        }

        Pointer[] bPointers = new Pointer[b.length];
        for (int i = 0; i <b.length; i++) {
            bPointers[i] = ((PBCElement) b[i]).getValue();
        }

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_multi_add(aPointers, aPointers, bPointers, a.length);

        return a;
    }

    public ElementPowPreProcessing pow(byte[] source) {
        return new PBCElementPowPreProcessing(this, source, 0);
    }

    public ElementPowPreProcessing pow(byte[] source, int offset) {
        return new PBCElementPowPreProcessing(this, source, offset);
    }

}
