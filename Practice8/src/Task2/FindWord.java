package Task2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;

public class FindWord {

    public static List<Callable<String>> callables = new ArrayList<>();
    public static boolean isFind = false;

    // 遍历 target directory to create a list of Callables
    public static List<Callable<String>> traverseDirectory(File directory, String word) throws Exception {

        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            for(File file: files){
                if (file.isDirectory()){
                    traverseDirectory(file, word);
                }else{
                    Callable<String> callable = new FindWordCallable(file,word);
                    callables.add(callable);
                }
            }
        }
        return callables;
    }


    public static void main(String[] args) throws Exception{

        ExecutorService executorService = Executors.newCachedThreadPool();
        // ExecutorService executorService = Executors.newSingleThreadExecutor();
        // ExecutorService executorService = Executors.newFixedThreadPool(10);

        File directory = new File("D:\\IDEA_project\\Fall2023\\Practice8\\srcJDK8");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter keyword (e.g. volatile):");
        String word = scanner.next();

        List<Callable<String>> callables = traverseDirectory(directory,word);
        String Name = executorService.invokeAny(callables);
        executorService.shutdown();

        System.out.println("Found the first file that contains " + word + ": " + Name);

        if (executorService instanceof ThreadPoolExecutor)
            System.out.println("Largest pool size: "
                    + ((ThreadPoolExecutor) executorService).getLargestPoolSize());

    }
}


// Define a Callable, which represents a task that counts the occurrence of the given word in a given file.
class FindWordCallable implements Callable<String> {
    private File file;
    private String word;
    public FindWordCallable(File file, String word){
        this.file = file;
        this.word = word;
    }

    public String call() throws Exception {
        try {
            int count = 0;
            for (String line : Files.readAllLines(file.toPath())) {
                String[] words = line.split(" ");
                for (String w : words) {
                    if (w.equalsIgnoreCase(word)) {
                        count+=1;
                    }
                }
            }

            if(count!=0){
                //System.out.println(file.getPath());
                FindWord.isFind = true;
                return file.getPath();
            }else{
                String Name = file.getPath();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Search in " + Name + " canceled.");
                    return null;
                }
                //throw new NoSuchElementException(file.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new NoSuchElementException();
    }
}

