import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PrimeFinder implements Callable<List<Integer>> {
    private int start; // Starting number of the range to check for primes
    private int end;   // Ending number of the range to check for primes

    // Constructor to initialize the range for prime checking
    public PrimeFinder(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Callable method to find all prime numbers in the specified range.
     * @return A list of prime numbers within the range [start, end].
     */
    @Override
    public List<Integer> call() {
        List<Integer> primes = new ArrayList<>(); // List to store prime numbers
        for (int num = start; num <= end; num++) { // Iterate through the range
            if (isPrime(num)) { // Check if the number is prime
                primes.add(num); // Add the number to the list if it is prime
            }
        }
        return primes; // Return the list of prime numbers
    }

    /**
     * Helper method to determine if a number is prime.
     * A number is prime if it is greater than 1 and divisible only by 1 and itself.
     * @param number The number to check.
     * @return True if the number is prime, false otherwise.
     */
    private boolean isPrime(int number) {
        if (number <= 1) return false; // Numbers <= 1 are not prime
        for (int i = 2; i * i <= number; i++) { // Loop from 2 to √number
            if (number % i == 0) return false; // If divisible, it's not prime
        }
        return true; // Number is prime
    }
}
