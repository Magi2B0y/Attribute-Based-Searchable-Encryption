package it.unisa.dia.gas.crypto.jpbc.utils;

import it.unisa.dia.gas.jpbc.*;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ElementUtils {

    public static Element[] cloneImmutable(Element[] source) {
        Element[] target = Arrays.copyOf(source, source.length);

        for (int i = 0; i < target.length; i++) {
            Element uElement = target[i];
            if (uElement != null && !uElement.isImmutable())
                target[i] = target[i].getImmutable();
        }

        return target;
    }

    public static ElementPow[] cloneToElementPow(Element[] source) {
        ElementPow[] target = new ElementPow[source.length];

        for (int i = 0; i < target.length; i++) {
            target[i] = source[i].pow();
        }

        return target;
    }

    public static Element randomIn(Pairing pairing, Element generator) {
        return generator.duplicate().powZn(pairing.getZr().newRandomElement());
    }

    public static Element getGenerator(Pairing pairing, Element generator, CurveParameters curveParameters, int subgroupIndex, int numPrimes) {
        BigInteger prod = BigInteger.ONE;
        for (int j = 0; j < numPrimes; j++) {
            if (j != subgroupIndex)
                prod = prod.multiply(curveParameters.getBigInteger("n" +j));
        }

        return generator.pow(prod);
    }

    public static void print(Element[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matrix[i][j] + ", ");

            }
            System.out.println();
        }
        System.out.println();
    }


    public static Element[][] transpose(Element[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {

            for (int j = i+1; j < n; j++) {

                Element temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        return matrix;
    }

    public static Element[][] multiply(Element[][] a, Element[][] b) {
        int n = a.length;
        Field field = a[0][0].getField();

        Element[][] res = new Element[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                res[i][j] = field.newZeroElement();
                for (int k = 0; k < n; k++)
                    res[i][j].add(a[i][k].duplicate().mul(b[k][j]));
            }
        }

        return res;
    }

    public static void copyArray(Element[][] target, Element[][] source, int sizeY, int sizeX, int y, int x) {
        for (int i = y; i < sizeY; i++) {
            for (int j = x; j < sizeX; j++) {
                target[i - y][j - x] = source[i][j].duplicate();
            }
        }
    }
}
