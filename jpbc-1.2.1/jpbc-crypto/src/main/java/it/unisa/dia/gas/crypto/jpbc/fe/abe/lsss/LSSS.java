package it.unisa.dia.gas.crypto.jpbc.fe.abe.lsss;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class LSSS {

    private Field Zr;

    public LSSS(Field zr) {
        Zr = zr;
    }

    public Element[][] getAccessStructure(String formula) {
        return null;
    }



    public static Element[][] gaussianElimination(Element[][] A) {
        // m : the number or rows
        // n : the number of cols
        int m = A.length;
        int n = A[0].length;

        Element[][] B = new Element[m][n];
        ElementUtils.copyArray(B, A, m, n, 0, 0);

        int r = 0;
        for (int j = 0; j < n; j++) {
//            System.out.println("j = " + j);
            int l = -1, i = r;

            while (l == -1 && i < m) {
                if (!B[i][j].isZero())
                    l = i;
                i++;
            }

            if (l != -1) {
                System.out.printf("r = %d, j = %d\n", r, j);
                // swap rows r and l of B
                for (int k = 0; k < n; k++) {
                    Element temp = B[r][k];
                    B[r][k] = B[l][k];
                    B[l][k] = temp;
                }

//                System.out.printf("swap (r=%d, l=%d}\n", r, l);
//                ElementUtils.print(B);

                // Row_r(B) = B[r,j]^-1 \cdot Row_r(B)
                for (int k = 0; k < n; k++) {
                    B[r][k].div(B[r][j]);
                }

//                System.out.println("Row_r(B) = B[r,j]^-1 \\cdot Row_r(B)");
//                ElementUtils.print(B);

                for (i = 0; i < m; i++) {
                    if (i != r) {
                        for (int k = 0; k < n; k++)
                            B[i][k].sub(B[i][j].duplicate().mul(B[r][k]));

//                        ElementUtils.print(B);
                    }
                }

                r++;
            }
        }
        System.out.println("r = " + r);


        return B;
    }


    public static void main(String[] args) {
        Field Zr = new ZrField(new BigInteger("17"));

        Element[][] A = new Element[3][3];

        int m = A.length;
        int n = A[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = Zr.newRandomElement();
            }
        }
//        A[0][0] = Zr.newElement(1);
//        A[0][1] = Zr.newElement(1);
//        A[0][2] = Zr.newElement(0);

        A[0][0] = Zr.newElement(0);
        A[0][1] = Zr.newElement(-1);
        A[0][2] = Zr.newElement(1);

        A[1][0] = Zr.newElement(0);
        A[1][1] = Zr.newElement(0);
        A[1][2] = Zr.newElement(-1);

        A[2][0] = Zr.newElement(0);
        A[2][1] = Zr.newElement(-1);
        A[2][2] = Zr.newElement(0);

        ElementUtils.print(A);
        Element[][] B = gaussianElimination(A);

        ElementUtils.print(B);
    }

}
