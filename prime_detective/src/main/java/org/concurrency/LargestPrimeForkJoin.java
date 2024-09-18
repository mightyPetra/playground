package org.concurrency;

import static java.lang.Runtime.getRuntime;

import java.math.BigInteger;
import java.time.Clock;
import java.util.concurrent.ForkJoinPool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LargestPrimeForkJoin {

    static int timeLimit = 5 * 1000;

    public static void main(String[] args) {
        Clock clock = Clock.systemDefaultZone();
        long start;
        long runtime;

        // Initial variables
        BigInteger largestPrime = BigInteger.valueOf(-1);
        BigInteger n = new BigInteger(
            "341550071728321341550071728321341550071728321341550071728321341550071728321341550071728321321341550071728321341550071728321341550071728321");
        int k = 160;
        start = clock.millis();

        int nThreads = getRuntime().availableProcessors();

        ForkJoinPool pool = new ForkJoinPool(nThreads);

        while (true) {
            // Starting n at an odd number, we ensure all numbers we check are odd by incrementing by 2
            n = n.add(BigInteger.valueOf(2));

            // Runtime calculated from start time
            runtime = clock.millis() - start;

            // If the n we are testing is prime, store it
            RecursivePrime rPrime = new RecursivePrime(n, k);
            pool.invoke(rPrime);

            if (rPrime.result) {
                log.info("Prime found... {}", n);
                largestPrime = n;
            }

            // Stopping criteria
            if (runtime > timeLimit) {
                break;
            }
        }
        if (largestPrime.compareTo(BigInteger.ZERO) < 0) {
            log.info("No primes found.");
        } else {
            log.info("Largest prime found: {}", largestPrime);
        }
    }
}
