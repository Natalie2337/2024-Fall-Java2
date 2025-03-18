package Demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WordCountDemo {
    public static void main(String[] args) throws Exception {
        // Get the target directory path
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path of the target directory: ");
        String directoryPath = scanner.nextLine();

        // Get the word to count
        System.out.print("Enter the word to count: ");
        String word = scanner.nextLine();

        // Create a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Traverse the target directory and create a list of tasks
        List<Callable<Integer>> tasks = new ArrayList<>();
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .forEach(file -> tasks.add(() -> countWordOccurrences(file, word)));

        // Run the tasks asynchronously and get back a list of future objects
        List<Future<Integer>> results = executor.invokeAll(tasks);

        // Sum all the future objects to compute the total occurrence of the word
        int totalOccurrences = 0;
        for (Future<Integer> result : results) {
            totalOccurrences += result.get();
        }

        // Shutdown the executor
        executor.shutdown();

        // Print the total occurrence of the word
        System.out.println("Total occurrences of \"" + word + "\" in all files: " + totalOccurrences);
    }

    private static int countWordOccurrences(Path file, String word) throws IOException {
        int occurrences = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                if (scanner.next().equals(word)) {
                    occurrences++;
                }
            }
        }
        return occurrences;
    }
}
