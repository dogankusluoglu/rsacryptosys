# RSAcryptosys
A basic small scale Java program that generates Public and Private keys for RSA encryption/decryption. Developed with the intent to test mathematical algorithms involved in RSA encryption/decryption such as the Extended Euclidean Algorithm and a square left to right multiply algorithm.

## Compilation
Simply compile using `javac RSAystem.java`

## Usage

### Main function 
`generateKeys(int  p,  int  q,  int  e)` Generates an array of RSA public and private keys for the given prime numbers.
* p and q and a number e relatively prime to (p-1)*(q-1).
* The returned integer array has the following form: {n, e, d} where n is the product of p and q, e is the public exponent, and d is the private exponent.
* Returns null if the input parameters are invalid.
### Utility functions
`isPrime(int  e)` is a naive calculation that checks whether the given integer e is a prime number or not.
* A prime number is a positive integer greater than 1 that has no positive integer divisors other than 1 and itself.

`getEulerTotient(int  n,  List<Integer>  factorsList)` Calculates the Euler totient function of the given integer n, using the prime factors provided in the factorsList.
* The Euler totient function, also known as Euler's phi function, is defined as the number of positive integers that are relatively prime to n.

`getPrimeFactorisation(int  n)`  Calculates the prime factorization of the given positive integer n.

* Prime factorization is the process of finding the prime factors of a number, which are the prime numbers that can divide the number exactly without leaving a remainder.

`getModularInverse(int  r0,  int  r1,  int  n)`  Calculates the modular inverse of r1 modulo n using the Extended Euclidean Algorithm.

* The Extended Euclidean Algorithm is an algorithm that calculates the greatest common divisor (GCD) of two integers, as well as the coefficients x and y of the Bezout's identity, which is an equation of the form ``ax + by = gcd(a, b)``.
* In the case of calculating a modular inverse, the coefficients x and y can be used to find the inverse of r1 modulo n, if it exists.
