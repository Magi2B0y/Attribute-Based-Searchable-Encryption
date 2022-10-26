package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeEPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.g.TypeGPairing;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {
    private static final PairingFactory INSTANCE = new PairingFactory();


    public static PairingFactory getInstance() {
        return INSTANCE;
    }

    public static Pairing getPairing(CurveParameters curveParameters) {
        return getInstance().initPairing(curveParameters);
    }

    public static Pairing getPairing(String curveParametersPath) {
        return getInstance().initPairing(curveParametersPath);
    }

    public static Pairing getPairing(CurveParameters curveParameters, Random random) {
        return getInstance().initPairing(curveParameters, random);
    }

    public static Pairing getPairing(String curveParametersPath, Random random) {
        return getInstance().initPairing(curveParametersPath, random);
    }


    private boolean usePBCWhenPossible = false;
    private boolean reuseInstance = true;

    private boolean pbcAvailable = false;
    private Method getPairingMethod;
    private Throwable pbcPairingFailure;

    private Map<CurveParameters, Pairing> instances;

    public PairingFactory() {
        // Try to load jpbc-pbc factory
        try {
            Class pbcPairingFactoryClass = Class.forName("it.unisa.dia.gas.plaf.jpbc.pbc.PairingFactory");
            Method isPBCAvailable = pbcPairingFactoryClass.getMethod("isPBCAvailable", null);
            pbcAvailable = ((Boolean)isPBCAvailable.invoke(null));
            if (pbcAvailable)
                getPairingMethod = pbcPairingFactoryClass.getMethod("getPairing", CurveParameters.class);
        } catch (Exception e) {
            pbcAvailable = false;
            pbcPairingFailure = e;
        }

        this.instances = new HashMap<CurveParameters, Pairing>();
    }

    public Pairing initPairing(String curveParametersPath) {
        return initPairing(loadCurveParameters(curveParametersPath), null);
    }

    public Pairing initPairing(CurveParameters curveParameters) {
        return initPairing(curveParameters, null);
    }

    public Pairing initPairing(String curveParametersPath, Random random) {
        return initPairing(loadCurveParameters(curveParametersPath), random);
    }

    public Pairing initPairing(CurveParameters curveParameters, Random random) {
        if (curveParameters == null)
            throw new IllegalArgumentException("curveParameters cannot be null.");

        Pairing pairing = null;
        if (reuseInstance && random == null) {
            pairing = instances.get(curveParameters);
            if (pairing != null)
                return pairing;
        }

        if (usePBCWhenPossible && pbcAvailable)
            pairing = getPBCPairing(curveParameters);

        if (pairing == null) {
            String type = curveParameters.getString("type");
            if ("a".equalsIgnoreCase(type))
                pairing = new TypeAPairing(random, curveParameters);
            else if ("a1".equalsIgnoreCase(type))
                pairing = new TypeA1Pairing(random, curveParameters);
            else if ("d".equalsIgnoreCase(type))
                pairing = new TypeDPairing(random, curveParameters);
            else if ("e".equalsIgnoreCase(type))
                pairing = new TypeEPairing(random, curveParameters);
            else if ("f".equalsIgnoreCase(type))
                return new TypeFPairing(random, curveParameters);
            else if ("g".equalsIgnoreCase(type))
                return new TypeGPairing(random, curveParameters);
            else
                throw new IllegalArgumentException("Type not supported. Type = " + type);
        }

        if (reuseInstance)
            instances.put(curveParameters, pairing);

        return pairing;
    }

    public boolean isUsePBCWhenPossible() {
        return usePBCWhenPossible;
    }

    public void setUsePBCWhenPossible(boolean usePBCWhenPossible) {
        this.usePBCWhenPossible = usePBCWhenPossible;
    }

    public boolean isReuseInstance() {
        return reuseInstance;
    }

    public void setReuseInstance(boolean reuseInstance) {
        this.reuseInstance = reuseInstance;
    }

    public Throwable getPbcPairingFailure() {
        return pbcPairingFailure;
    }

    public CurveParameters loadCurveParameters(String path) {
        DefaultCurveParameters curveParams = new DefaultCurveParameters();
        curveParams.load(path);

        return curveParams;
    }

    public Pairing getPBCPairing(CurveParameters curveParameters) {
        try {
            return (Pairing) getPairingMethod.invoke(null, curveParameters);
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    public boolean isPBCAvailable() {
        return pbcAvailable;
    }

}
