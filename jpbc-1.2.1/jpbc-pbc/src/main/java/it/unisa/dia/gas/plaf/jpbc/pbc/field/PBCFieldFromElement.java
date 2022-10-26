package it.unisa.dia.gas.plaf.jpbc.pbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCElement;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCField;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCFieldFromElement extends PBCField {
    protected PBCElementType pbcElement;


    public PBCFieldFromElement(PBCElementType pbcElement) {
        super(null);

        this.pbcElement = pbcElement;
        this.fixedLengthInBytes = WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(pbcElement);

        MPZElementType mpzOrder = new MPZElementType();
        mpzOrder.init();
        WrapperLibraryProvider.getWrapperLibrary().pbc_field_order(pbcElement, mpzOrder);
        this.order = new BigInteger(mpzOrder.toString(10));
    }

    public Element newElement() {
        PBCElementType element = new PBCElementType();
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_init_same_as(element, pbcElement);

        return new PBCElement(element, this);
    }
}
