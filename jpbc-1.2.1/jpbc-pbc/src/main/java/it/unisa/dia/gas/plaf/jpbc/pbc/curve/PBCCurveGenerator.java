package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class PBCCurveGenerator implements CurveGenerator {

    public CurveParameters generate() {
        pbcGenerate("jpbc_pbc_params.prm");

        DefaultCurveParameters curveParams;
        try {
            curveParams = new DefaultCurveParameters();
            File file = new File("jpbc_pbc_params.prm");
            if (!file.exists())
                throw new IllegalStateException("Failed to load parameters.");

            FileInputStream inputStream = new FileInputStream("jpbc_pbc_params.prm");
            curveParams.load(inputStream);
            inputStream.close();

            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return curveParams;
    }


    protected abstract void pbcGenerate(String fileName);

}
