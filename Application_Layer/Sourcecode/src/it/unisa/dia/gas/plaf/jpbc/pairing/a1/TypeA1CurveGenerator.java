package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1CurveGenerator implements CurveGenerator, Serializable {

    private static final long serialVersionUID = -1L;
    protected Random random;
    protected int numPrimes, bits;


    public TypeA1CurveGenerator(Random random, int numPrimes, int bits) {
        this.random = random;
        this.numPrimes = numPrimes;
        this.bits = bits;
    }

    public TypeA1CurveGenerator(int numPrimes, int bits) {
        this(new SecureRandom(), numPrimes, bits);
    }


    public CurveParameters generate() {
        BigInteger[] primes = new BigInteger[numPrimes];
        BigInteger order, n, p;
        long l;

        while (true) {
            System.out.printf("Finding order...\n");
            while (true) {
                order = BigInteger.ONE;
                for (int i = 0; i < numPrimes; i++) {

                    boolean isNew = false;
                    while (!isNew) {
//                        primes[i] = BigIntegerUtils.generateSolinasPrime(bits, random);
                        primes[i] = BigInteger.probablePrime(bits, random);
                        isNew = true;
                        for (int j = 0; j < i; j++) {
                            if (primes[i].equals(primes[j])) {
                                isNew = false;
                                break;
                            }
                        }
                    }

                    order = order.multiply(primes[i]);
                }

                break;
//                if ((order.bitLength() + 7) / 8 == order.bitLength() / 8)
//                    break;
            }
            System.out.printf("order= %s\n", order);

            // If order is even, ideally check all even l, not just multiples of 4
            System.out.printf("Finding l...\n");
            l = 4;
            n = order.multiply(BigIntegerUtils.FOUR);

            p = n.subtract(BigInteger.ONE);
            while (!p.isProbablePrime(10)) {
                p = p.add(n);
                l += 4;
            }
            System.out.printf("l=%d\n",l);

            System.out.printf("lhs=%d, rhs=%d\n",(p.bitLength() + 7) / 8, (p.bitLength() / 8));

//            if ((p.bitLength() + 7) / 8 == p.bitLength() / 8)
//                break;
            break;

//            System.out.printf("No way, repeat!\n");
        }
        System.out.printf("order hamming weight=%d\n", BigIntegerUtils.hammingWeight(order));

        DefaultCurveParameters params = new DefaultCurveParameters();
        params.put("type", "a1");
        params.put("p", p.toString());
        params.put("n", order.toString());
        for (int i = 0; i < primes.length; i++) {
            params.put("n" + i, primes[i].toString());
        }
        params.put("l", String.valueOf(l));


        return params;
    }

    public static void main(String[] args) {
        TypeA1CurveGenerator generator = new TypeA1CurveGenerator(3, 512);
        DefaultCurveParameters curveParams = (DefaultCurveParameters) generator.generate();

        System.out.println(curveParams.toString(" "));
    }

}
