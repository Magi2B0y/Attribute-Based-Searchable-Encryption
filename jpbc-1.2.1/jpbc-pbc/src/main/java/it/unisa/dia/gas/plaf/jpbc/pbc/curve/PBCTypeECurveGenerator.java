package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCTypeECurveGenerator extends PBCCurveGenerator {
    protected int rbits, qbits;


    public PBCTypeECurveGenerator(int rbits, int qbits) {
        this.rbits = rbits;
        this.qbits = qbits;
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_e(fileName, rbits, qbits);
    }


    public static void main(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("Too few arguments. Usage <rbits> <qbits>");

        if (args.length > 2)
            throw new IllegalArgumentException("Too many arguments. Usage <rbits> <qbits>");

        Integer rBits = Integer.parseInt(args[0]);
        Integer qBits = Integer.parseInt(args[1]);

        CurveGenerator generator = new PBCTypeECurveGenerator(rBits, qBits);
        DefaultCurveParameters curveParams = (DefaultCurveParameters) generator.generate();

        System.out.println(curveParams.toString(" "));
    }

}