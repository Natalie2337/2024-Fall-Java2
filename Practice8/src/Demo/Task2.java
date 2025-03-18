package Demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Task2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner in = new Scanner(System.in);

//        int processors = Runtime.getRuntime().availableProcessors();
//        ExecutorService ThreadPool = Executors.newFixedThreadPool(processors);
        ExecutorService ThreadPool = Executors.newCachedThreadPool();
//        ExecutorService ThreadPool = Executors.newSingleThreadExecutor();

        System.out.println("Enter keyword (e.g. volatile):");
        String word = in.next();

        List<String> fileList;
        try(Stream<Path> walk = Files.walk(Paths.get("C:\\Users\\86136\\Desktop\\大四上\\CS209A_java2\\Lab\\src"))) {
            fileList = walk.filter(Files::isRegularFile).map(path ->path.getParent()+"/"+path.getFileName().toString()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Callable<Path>> callableList = new ArrayList<>();
        for (String file : fileList)
            callableList.add(new TaskTwoCallable(Path.of(file), word));
        Path res = ThreadPool.invokeAny(callableList);
        ThreadPool.shutdown();

        System.out.println("Found the first file that contains " +
                word + ": " + res);

        if (ThreadPool instanceof ThreadPoolExecutor)
            System.out.println("Largest pool size: "
                    + ((ThreadPoolExecutor) ThreadPool).getLargestPoolSize());
    }
}

