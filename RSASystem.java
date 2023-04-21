import java.util.ArrayList;
import java.util.List;

public class RSASystem {
    public static void main(String[] args) {

        int n[] = generateKeys(61,67,17);

        for (int i = 0; i < 3; i++) {
            System.out.println("Key: " + n[i]);
        }

    }

    // Returns null on invalid params
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

        int keys[] = {n, e, d};
        return keys;
    }

    /* Naive process checking if arg is prime */
    public static boolean isPrime(int e) {
        if (e < 2) return false;
        if (e == 2) return true;
        if (e % 2 == 0) return false;

        int square = (int) Math.sqrt(e);

        for (int i = 3; i < square; i++) {
            if (e % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static int getEulerTotient(int n, List<Integer> factorsList) {
        int totient = 1;

        // Calculate Euler's Totient
        if (factorsList.size() == 1) {
            totient = factorsList.get(0) - 1;
            return totient;
        }
        int counter = 1; 

        for (int i = 0; i < factorsList.size() - 1; i++) {
            int curFact = factorsList.get(i), nextFact = factorsList.get(i+1); 

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

    public static List<Integer> getPrimeFactorisation(int n) {
        // Prime factorisation
        List<Integer> factorsList = new ArrayList<Integer>();

        int square = (int) Math.sqrt(n);

        while (n % 2 == 0) {
            factorsList.add(2);
            n = n/2;
        }

        for (int i = 3; i < square; i+=2) {
            while (n % i == 0) {
                factorsList.add(i);
                n = n/i;
            }
        }

        if (n > 2) factorsList.add(n);

        return factorsList;
    }

    // Calculates the modular inverse using the Extended Euclidean Algorithm
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
}