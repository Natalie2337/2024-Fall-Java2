package Task1;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WordCount{

    public static List<Callable<Integer>> callables = new ArrayList<>();

    // 遍历 target directory to create a list of Callables
    public static List<Callable<Integer>> traverseDirectory(File directory, String word) throws Exception {
        //List<Callable<Integer>> callables = new ArrayList<>();
        /*
        // Files.walk方法遍历目录，并使用 Files::isRegularFile方法引用过滤出所有的常规文件。
        Files.walk(directory)
                .filter(Files::isDirectory)
                .forEach(
                        path->{Callable<Integer> callable = new WordCountCallable(path.toFile(),word);
                        callables.add(callable);
                });
         */
        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            for(File file: files){
                if (file.isDirectory()){
                    traverseDirectory(file, word); //
                }else{
                    Callable<Integer> callable = new WordCountCallable(file,word);
                    callables.add(callable);
                }
            }
        }
        return callables;
    }


    public static void main(String[] args) throws Exception{
        // Create a thread pool using the static factory methods of Executors.
        // ExecutorService：是一个比Executor使用更广泛的子类接口，其提供了生命周期管理的方法，返回 Future 对象，以及可跟踪一个或多个异步任务执行状况返回Future的方法
        // ExecutorService.submit()可以接受Runnable和Callable接口的对象,返回的Future对象

        //Executors 类提供工厂方法用来创建不同类型的线程池。
        // newSingleThreadExecutor() 创建一个只有一个线程的线程池，
        // newFixedThreadPool(int numOfThreads)来创建固定线程数的线程池，
        // newCachedThreadPool()可以根据需要创建新的线程，但如果已有线程是空闲的会重用已有线程。

        Instant startTime = Instant.now();

        ExecutorService executorService = Executors.newCachedThreadPool();
        // ExecutorService executorService = Executors.newSingleThreadExecutor();
        // ExecutorService executorService = Executors.newFixedThreadPool(10);

        File directory = new File("D:\\IDEA_project\\Fall2023\\Practice8\\srcJDK8");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter keyword (e.g. volatile):");
        String word = scanner.next();

        List<Callable<Integer>> callables = traverseDirectory(directory,word);
        // Use executor's invokeAll method to run the list of tasks asynchronously, and get back a list of Future objects.
        List<Future<Integer>> futures = executorService.invokeAll(callables);

        int Occurrences = 0;
        for (Future<Integer> future:futures) {
            Occurrences += future.get();
        }

        System.out.println("Occurrences of "+word+": "+Occurrences);
        executorService.shutdown();

        Instant endTime = Instant.now();
        System.out.println("Time elapsed: "
                + Duration.between(startTime, endTime).toMillis() + " ms\n");
    }
}




// Define a Callable, which represents a task that counts the occurrence of the given word in a given file.
class WordCountCallable implements Callable<Integer> {
    private File file;
    private String word;
    public WordCountCallable(File file, String word){
        this.file = file;
        this.word = word;
    }

    public Integer call() throws Exception {
        int count = 0;
        for (String line : Files.readAllLines(file.toPath())) {
            String[] words = line.split(" ");
            for (String w : words) {
                if (w.equalsIgnoreCase(word)) {
                    count++;
                }
            }
        }
        return count;
    }
}

