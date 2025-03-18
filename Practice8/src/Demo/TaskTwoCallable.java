package Demo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class TaskTwoCallable implements Callable<Path> {

    private final Path path;

    private final String word;

    TaskTwoCallable(Path path, String word) {

        this.path = path;
        this.word = word;
    }

    @Override
    public Path call() throws NoSuchFieldException {
        try (var in = new Scanner(path)) {
            while (in.hasNext()) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Search in " + path + " canceled.");
                    return null;
                }
                String w = in.next();
                if (word.equals(w)){
                    return this.path;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new NoSuchFieldException();
    }
}
