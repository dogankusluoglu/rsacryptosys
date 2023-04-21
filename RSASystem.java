
/**
* The RSASystem program provides functionality to generate public and private keys for 
*   the RSA encryption scheme.
* 
* The program contains a generateKeys() function that takes two prime numbers p and q,
*   as well as an integer e, and calculates the public and private keys for the RSA 
*   encryption scheme. The public key consists of the modulus n and the encryption 
*   exponent e, while the private key consists of the modulus n and the decryption 
*   exponent d.

* The program uses various mathematical operations, such as prime factorization, 
*   Euler's totient function, and the Extended Euclidean Algorithm, to calculate 
*   the keys. The program also includes utility functions to calculate 
*   prime factorization, large exponents and modular inverse.
*
* @author  Dogan Kusluoglu
* @version 1.0
* @since   2023-04-20 
*
* @see <a href="https://en.wikipedia.org/wiki/RSA_(cryptosystem)">RSA Cryptosystem</a>
* @see <a href="https://en.wikipedia.org/wiki/Prime_factorization">Prime Factorization</a>
* @see <a href="https://en.wikipedia.org/wiki/Euler%27s_totient_function">Euler's Totient Function</a>
* @see <a href="https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm">Extended Euclidean Algorithm</a>
*/

import java.util.ArrayList;
import java.util.List;

public class RSASystem {
    public static void main(String[] args) {

        int keys[] = generateKeys(61, 67, 17);

        char msgAlph[] = ("MEET AT NINE").toCharArray();
        int ciphers[] = new int[msgAlph.length];
        char decrypted[] = new char[msgAlph.length];

        System.out.println("Encrypted message:");
        for (int i = 0; i < msgAlph.length; i++) {
            int m = ((int) msgAlph[i]) - 64;
            ciphers[i] = encrypt(m, keys[0], keys[1]);
            System.out.print(ciphers[i] + " ");
        }
        System.out.println();

        System.out.println("Decrypted message:");
        for (int i = 0; i < msgAlph.length; i++) {
            int m = decrypt(ciphers[i], keys[0], keys[2]) + 64;
            char c = (char) m;
            decrypted[i] = c;
            System.out.print(decrypted[i] + " ");
        }
    }

    /**
     * Generates an array of RSA public and private keys for the given prime numbers
     * p and q and a number e relatively prime to (p-1)*(q-1).
     * 
     * The returned integer array has the following form: {n, e, d} where n is the
     * product of p and q, e is the public exponent, and d is the private exponent.
     * 
     * Returns null if the input parameters are invalid.
     * 
     * @param p a prime number to be used in key generation
     * @param q a prime number to be used in key generation
     * @param e a number relatively prime to (p-1)*(q-1) to be used as the public
     *          exponent
     * @return an array of integers containing the public and private keys in the
     *         form of {n, e, d} at index 0, 1, and 2, respectively, or null if
     *         invalid parameters are given.
     */
    public static int[] generateKeys(int p, int q, int e) {

        int n, eulerN, d;

        if (!(isPrime(p) && isPrime(q))) {
            System.out.printf("Prime status:\np = %d: %b\n", p, isPrime(p));
            System.out.printf("q = %d: %b\n", q, isPrime(q));
            return null;
        }

        n = p * q;

        List<Integer> primes = getPrimeFactorisation(n);
        List<Integer> expPrimes = getPrimeFactorisation(e);

        for (Integer integer : expPrimes) {
            if (n % integer == 0) {
                System.out.println("n and e are not relatively prime, choose different values");
                return null;
            }
        }

        eulerN = getEulerTotient(n, primes);

        for (Integer integer : expPrimes) {
            if (eulerN % integer == 0) {
                System.out.println("phi(n) and e are not relatively prime, choose different values");
                return null;
            }
        }

        d = getModularInverse(eulerN, e, n);

        int keys[] = { n, e, d };
        return keys;
    }

    /**
     * Naive calculation that checks whether the given integer e is a prime number
     * or not.
     * A prime number is a positive integer greater than 1 that has no positive
     * integer divisors other than 1 and itself.
     * 
     * @param e the integer to be checked for primality
     * @return true if the integer is a prime number, false otherwise
     */
    public static boolean isPrime(int e) {
        if (e < 2)
            return false;
        if (e == 2)
            return true;
        if (e % 2 == 0)
            return false;

        int square = (int) Math.sqrt(e);

        for (int i = 3; i < square; i++) {
            if (e % i == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates the Euler totient function of the given integer n, using the prime
     * factors provided in the factorsList.
     * The Euler totient function, also known as Euler's phi function, is defined as
     * the number of positive integers that are relatively prime to n.
     * 
     * @param n           the modulus for which the Euler totient function needs to
     *                    be calculated
     * @param factorsList a list of prime factors of n
     * @return an integer representing the value of Euler totient function of n
     */
    public static int getEulerTotient(int n, List<Integer> factorsList) {
        int totient = 1;

        // Calculate Euler's Totient
        if (factorsList.size() == 1) {
            totient = factorsList.get(0) - 1;
            return totient;
        }
        int counter = 1;

        for (int i = 0; i < factorsList.size() - 1; i++) {
            int curFact = factorsList.get(i), nextFact = factorsList.get(i + 1);

            if (curFact == nextFact) {
                counter++;
            } else {
                // Only one factor of this value
                if (counter == 1) {
                    totient = totient * (curFact - 1);
                } else { // Calc factor to the power of counter minus one times (factor - 1)
                    totient = (int) (totient * (Math.pow(curFact, counter - 1) * (curFact - 1)));
                }
                counter = 1;
            }
        }

        /*
         * Unhandled cases from for loop:
         * -- If 2nd last value is the same as the last value
         * -- If last value is different to second last value
         */
        int size = factorsList.size();

        if (factorsList.get(size - 2) == factorsList.get(size - 1)) {
            int fact = factorsList.get(size - 2);
            totient = (int) (totient * (Math.pow(fact, counter - 1) * (fact - 1)));
        } else {
            totient = totient * (factorsList.get(size - 1) - 1);
        }

        return totient;
    }

    /**
     * Calculates the prime factorization of the given positive integer n.
     * Prime factorization is the process of finding the prime factors of a number,
     * which are the prime numbers that can divide the number exactly without
     * leaving a remainder.
     * 
     * @param n the positive integer for which the prime factorization needs to be
     *          calculated
     * @return a List of integers containing the prime factors of n in ascending
     *         order
     */
    public static List<Integer> getPrimeFactorisation(int n) {
        // Prime factorisation
        List<Integer> factorsList = new ArrayList<Integer>();

        int square = (int) Math.sqrt(n);

        while (n % 2 == 0) {
            factorsList.add(2);
            n = n / 2;
        }

        for (int i = 3; i < square; i += 2) {
            while (n % i == 0) {
                factorsList.add(i);
                n = n / i;
            }
        }

        if (n > 2)
            factorsList.add(n);

        return factorsList;
    }

    /**
     * Calculates the modular inverse of r1 modulo n using the Extended Euclidean
     * Algorithm.
     * The Extended Euclidean Algorithm is an algorithm that calculates the greatest
     * common divisor (GCD) of two integers, as well as the coefficients x and y of
     * the Bezout's identity, which is an equation of the form ax + by = gcd(a, b).
     * In the case of calculating a modular inverse, the coefficients x and y can be
     * used to find the inverse of r1 modulo n, if it exists.
     * 
     * @param r0 an integer representing the first number in the Bezout's identity
     *           equation
     * @param r1 an integer representing the second number in the Bezout's identity
     *           equation, for which the inverse is to be calculated
     * @param n  an integer representing the modulus
     * @return an integer representing the modular inverse of r1 modulo n
     */
    public static int getModularInverse(int r0, int r1, int n) {
        int x0 = 1, x1 = 0, y0 = 0, y1 = 1;

        while (r1 != 0) {
            int q = Math.floorDiv(r0, r1);

            int rtmp = r1;
            r1 = r0 - (r1 * q);
            r0 = rtmp;

            int xtmp = x1;
            x1 = x0 - (x1 * q);
            x0 = xtmp;

            int ytmp = y1;
            y1 = y0 - (y1 * q);
            y0 = ytmp;
        }

        if (y0 < 0) {
            y0 += n;
        }

        return y0;
    }

    /**
     * 
     * Encrypts a plaintext message using the RSA algorithm.
     * 
     * Given the plaintext message m, modulus n and public key e, this method
     * calculates the ciphertext.
     * 
     * @param m the plaintext message to be encrypted
     * @param n the modulus used in the RSA algorithm
     * @param e the public key used in the RSA algorithm
     * @return the encrypted ciphertext
     */
    public static int encrypt(int m, int n, int e) {
        int c = squareLeftRightMultiply(m, e, n);

        return c;
    }

    /**
     * 
     * Decrypts an encrypted message using the RSA algorithm.
     * 
     * Given the ciphertext c, modulus n and private key e, this method calculates
     * the plaintext message.
     * 
     * @param c the ciphertext to be decrypted
     * @param n the modulus used in the RSA algorithm
     * @param e the private key used in the RSA algorithm
     * @return the decrypted plaintext message
     */
    public static int decrypt(int c, int n, int e) {
        int m = squareLeftRightMultiply(c, e, n);

        return m;
    }

    /**
     * 
     * Computes the modular exponentiation using the square and multiply
     * method.
     * Given the base b, exponent e and modulus n, this method calculates (b^e) mod
     * n.
     * 
     * @param b the base of the modular exponentiation
     * @param e the exponent of the modular exponentiation
     * @param n the modulus of the modular exponentiation
     * @return the result of the modular exponentiation (b^e) mod n
     */
    public static int squareLeftRightMultiply(int b, int e, int n) {
        int r = 1;
        while (e != 0) {
            if (e % 2 == 0) {
                e = e / 2;
                b = (int) Math.pow(b, 2) % n;
            } else {
                e = (e - 1) / 2;
                r = (r * b) % n;
                b = (int) Math.pow(b, 2) % n;
            }
        }
        return r;
    }
}