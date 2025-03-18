import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReadSourceCode {
    public static void main(String[] args) throws IOException {
        //找到jdk的路径
        //String jdkDirectory = System.getProperty("java.home");
        //找到当下的工作路径
        String zipdirectory = System.getProperty("user.dir");

        //src.zip的路径。
        //创建一个Path对象，表示多个路径组成的路径序列，比如Path path = Paths.get("C:\\path\\to", "file.txt");
        //Path path = Paths.get(jdkDirectory,"src.zip");
        Path path = Paths.get(zipdirectory, "src.zip");
        //System.out.format("toString: %s%n", path.toString());

        /*
        zip文件这样好像读不了子目录
        //利用resolve拼接path
        Path pathio = path.resolve("java/io"); // 写“/java/io”是错误的
        Path pathnio = path.resolve("java/nio");
        System.out.format("toString: %s%n", pathio.toString());
         */

        try {
            // 使用ZipInputStream
            //ZipInputStream类是Java中用于读取ZIP文件的输入流。它允许您逐个获取ZIP文件中的条目，并从中读取数据
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path.toString()));
            ZipEntry entry = zipInputStream.getNextEntry();
            int count = 0;
            while (entry != null) {
                // 输出条目名称。entryname很类似路径，类似这样"java/nio/channels/spi/AbstractSelector.java" 根据开头和结尾是否为符合条件的文件
                String entryName = entry.getName();
                //System.out.println(entryName);
                if (entryName.startsWith("java/io") || entryName.startsWith("java/nio")) {
                    if(entryName.endsWith(".java")){
                        count ++;
                        System.out.format(entryName.toString() + "\n");
                    }

                }
                entry = zipInputStream.getNextEntry(); //这一行一定要加，否则会停在原地不动进入死循环
            }
            zipInputStream.close();
            System.out.println(count);
            System.out.format("In .zip: # of .java files in java.io/java.nio packages: %d", count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    /*
    private static int countFile(Path directory) throws IOException{
        int count = 0;
        //
        // 也有用DirectoryStream<Path>来遍历的。DirectoryStream<Path>是Java.nio中的接口，用于遍历一个目录中的文件和子目录
        File[] files = directory.toFile().listFiles();
        if(files!=null){
            for (File file: files) {
                if(file.isFile() && file.getName().endsWith(".java")){
                    count ++;
                    System.out.println(file.getName());
                }
            }
        }
        return count;
    }
     */
    }
}
