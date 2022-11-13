package it.unisa.dia.gas.jpbc;

/**
 * This interface lets the user to generate all the necessary curve parameters
 * to initialize an instance of Pairing interface.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface CurveGenerator {

    /**
     * Generates the curve parameters.
     *
     * @return a map with all the necessary parameters.
     * @since 1.0.0
     */
    CurveParameters generate();

}
