package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;

import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class HVEIP08AbstractTest extends AbstractJPBCCryptoTest {

    public HVEIP08AbstractTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    protected int[][] createMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            if (i != 0  && i != 1 && random.nextBoolean()) {
                // it's a star
                result[0][i] = -1;
                result[1][i] = random.nextInt(2);
            } else {
                result[0][i] = random.nextInt(2);
                result[1][i] = result[0][i];
            }
        }
        return result;
    }

    protected int[][] createAllStarMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            result[0][i] = -1;
            result[1][i] = random.nextInt(2);
        }
        return result;
    }

    protected int[][] createNonMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            if (i != 0  && i != 1 && random.nextBoolean()) {
                // it's a star
                result[0][i] = -1;
                result[1][i] = random.nextInt(2);
            } else {
                result[0][i] = random.nextInt(2);
                result[1][i] = 1 - result[0][i];
            }
        }
        return result;
    }

}
