import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    private static final int START = 1;        // Starting point of the range for prime number search
    private static final int END = 1000;       // Ending point of the range for prime number search
    private static final int RANGE_SIZE = 100; // Size of each segment for dividing the range

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;

        // Loop to get valid user input within the defined range
        while (true) {
            System.out.print("Enter the upper limit for prime checking (range: " + START + " to " + END + "): ");
            n = scanner.nextInt();
            if (n >= START && n <= END) {
                break; // Valid input; exit loop
            } else {
                System.out.println("N is out of the range! It must be between " + START + " and " + END + ".");
            }
        }

        ExecutorService executor = Executors.newCachedThreadPool(); // Thread pool to manage tasks
        List<Future<List<Integer>>> results = new CopyOnWriteArrayList<>(); // Stores results of tasks
        long startTime = System.nanoTime(); // Start time for performance measurement

        try {
            // Divide the range into segments and assign tasks to the thread pool
            for (int i = START; i <= n; i += RANGE_SIZE) {
                int end = Math.min(i + RANGE_SIZE - 1, n); // Ensure the range doesn't exceed the upper limit
                Future<List<Integer>> future = executor.submit(new PrimeFinder(i, end)); // Submit task
                results.add(future); // Save the Future object for later processing
            }

            executor.shutdown(); // Initiates an orderly shutdown of the thread pool
            executor.awaitTermination(10, TimeUnit.SECONDS); // Wait for tasks to finish or timeout after 10 seconds

            // Collect and print results from all completed tasks
            for (Future<List<Integer>> result : results) {
                if (result.isDone()) { // Task completed successfully
                    List<Integer> primes = result.get(); // Retrieve the list of prime numbers
                    System.out.println("Primes found: " + primes);
                }
                if (result.isCancelled()) { // Task was cancelled
                    System.out.println("Task was cancelled.");
                }
            }
        } catch (Exception e) {
            // Handle exceptions during execution, including task failures
            System.out.println("Error occurred: " + e.getMessage());
        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow(); // Force shutdown of any remaining tasks
            }
        }

        long endTime = System.nanoTime(); // End time for performance measurement
        System.out.println("Execution Time: " + String.format("%.2f ms", (endTime - startTime) / 1_000_000.0));
    }
}
