
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class demo {
    public static void main(String[] args) {
        // Read the file
        File file = new File("D:\\IDEA_project\\Fall2023\\lab2_countwords\\src\\alice.txt");
        try {
            Scanner scanner = new Scanner(file);

            // Create a map to store word frequencies
            Map<String, Integer> wordFrequencyMap = new HashMap<>();

            // Process each word in the file
            while (scanner.hasNext()) {
                String word = scanner.next();
                wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
            }

            // Sort the words by their frequencies
            List<Map.Entry<String, Integer>> sortedWordFrequencyList = new ArrayList<>(wordFrequencyMap.entrySet());
            sortedWordFrequencyList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            // Print the top 5 words
            for (int i = 0; i < Math.min(5, sortedWordFrequencyList.size()); i++) {
                System.out.println(sortedWordFrequencyList.get(i).getKey() + ": " +
                        sortedWordFrequencyList.get(i).getValue());
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }
}
