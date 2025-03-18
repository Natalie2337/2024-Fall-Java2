import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * @version 1.03 2018-03-17
 * @author Cay Horstmann
 */
public class BlockingQueueTest
{
    private static final int FILE_QUEUE_SIZE = 10;
    private static final int SEARCH_THREADS = 100;
    private static final Path DUMMY = Path.of("");

    private static BlockingQueue<Path> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

    public static void main(String[] args)
    {
        try (var in = new Scanner(System.in))
        {
            // use current working directory
            String directory = System.getProperty("user.dir");

            System.out.print("Enter keyword (e.g. volatile): ");
            String keyword = in.nextLine();

            // producer
            Runnable enumerator = () -> {
                try
                {
                    enumerate(Path.of(directory));
                    queue.put(DUMMY);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (InterruptedException e)
                {
                }
            };

            new Thread(enumerator).start();

            // consumer
            for (int i = 1; i <= SEARCH_THREADS; i++) {
                Runnable searcher = () -> {
                    try
                    {
                        var done = false;
                        while (!done)
                        {
                            Path file = queue.take();
                            if (file == DUMMY)
                            {
                                queue.put(file);
                                done = true;
                            }
                            else search(file, keyword);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (InterruptedException e)
                    {
                    }
                };
                new Thread(searcher).start();
            }
        }
    }

    /**
     * Recursively enumerates all files in a given directory and its subdirectories.
     * See Chapters 1 and 2 of Volume II for the stream and file operations.
     * @param directory the directory in which to start
     */
    public static void enumerate(Path directory) throws IOException, InterruptedException
    {
        try (Stream<Path> children = Files.list(directory))
        {
            for (Path child : children.collect(Collectors.toList()))
            {
                if (Files.isDirectory(child))
                    enumerate(child);
                else {
                    if(child.getFileName().toString().endsWith(".java")) {
                        queue.put(child);
                    }
                }

            }

        }
    }

    /**
     * Searches a file for a given keyword and prints all matching lines.
     * @param file the file to search
     * @param keyword the keyword to search for
     */
    public static void search(Path file, String keyword) throws IOException
    {
        try (var in = new Scanner(file, StandardCharsets.UTF_8))
        {
            int lineNumber = 0;
            while (in.hasNextLine())
            {
                lineNumber++;
                String line = in.nextLine();
                if (line.contains(keyword))
                    System.out.printf("%s:%d:%s%n", file, lineNumber, line);
            }
        }
    }
}
