package it.unisa.dia.gas.plaf.jpbc.util.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * solves x^2 - d y^2 = n
 * D not a square
 * (for square D, observe (x+Dy)(x-Dy) = N and look at factors of N)
 */
public class PellEquation {
    public BigInteger D;
    public int N;

    public int count;
    public BigInteger minx, miny;
    public BigInteger[] x, y;


    public PellEquation(BigInteger D, int N) {
        this.D = D;
        this.N = N;
    }

    public void solve() {
        //TODO: brute force for small D

        // find square factors of N
        List<Integer> listOfF = new ArrayList<Integer>();
        int f = 1;
        while (true) {
            int n = f * f;
            if (n > Math.abs(N))
                break;

            if (Math.abs(N) % n == 0) {
                listOfF.add(f);
            }
            f++;
        }

        // a0, twice_a0 don't change once initialized
        // a1 is a_i every iteration
        // P0, P1 become P_{i-1}, P_i every iteration
        // similarly for Q0, Q1
        // variables to compute the convergents

        BigInteger a0 = BigIntegerUtils.sqrt(D);
        BigInteger P0;
        BigInteger Q0;

        BigInteger P1 = a0;
        BigInteger Q1 = D.subtract(a0.multiply(a0));
        BigInteger a1 = a0.add(P1).divide(Q1);
        BigInteger twice_a0 = a0.add(a0);

        BigInteger p0 = a0;
        BigInteger q0 = BigInteger.ONE;
        BigInteger p1 = a0.multiply(a1).add(BigInteger.ONE);
        BigInteger q1 = a1;

        int d = -1;
        int sgnN = N > 0 ? 1 : -1;
        List<BigInteger> listOfP = new ArrayList<BigInteger>();
        List<BigInteger> listOfQ = new ArrayList<BigInteger>();
        while (true) {
            if (d == sgnN) {
                for (int  i = 0; i < listOfF.size(); i++) {
                    f = listOfF.get(i);

                    if (Q1.compareTo(BigInteger.valueOf(Math.abs(N) / (f * f))) == 0) {
                        listOfP.add(p0.multiply(BigInteger.valueOf(f)));
                        listOfQ.add(q0.multiply(BigInteger.valueOf(f)));
                    }
                }
            }

            if (twice_a0.compareTo(a1) == 0 && d == 1)
                break;

            // compute more of the continued fraction expansion
            P0 = P1;
            P1 = a1.multiply(Q1);
            P1 = P1.subtract(P0);
            Q0 = Q1;
            Q1 = P1.multiply(P1);
            Q1 = D.subtract(Q1);
            Q1 = Q1.divide(Q0);
            a1 = a0.add(P1).divide(Q1);

            // compute next convergent
            BigInteger pnext = a1.multiply(p1).add(p0);
            p0 = p1;
            p1 = pnext;

            BigInteger qnext = a1.multiply(q1).add(q0);
            q0 = q1;
            q1 = qnext;

            d = -d;
        }
        listOfF.clear();

        minx = p0;
        miny = q0;
        count = listOfP.size();
        if (count != 0) {
            x = new BigInteger[count];
            y = new BigInteger[count];
            for (int i = 0; i < count; i++) {
                x[i] = listOfP.get(i);
                y[i] = listOfQ.get(i);
            }
        }
    }
}
