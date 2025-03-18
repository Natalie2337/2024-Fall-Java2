import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class ReadByteFile {
    public static void main(String[] args) throws IOException {
        //找到jdk的路径
        //String jdkDirectory = System.getProperty("java.home");
        //找到当下的工作路径
        String jardirectory = System.getProperty("user.dir");

        //rt.jar的路径。
        Path path = Paths.get(jardirectory, "rt.jar");
        //System.out.format("toString: %s%n", path.toString());

        try {
            // 使用JarInputStream
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(path.toString()));
            JarEntry entry = jarInputStream.getNextJarEntry();
            int count = 0;
            while (entry != null) {
                String entryName = entry.getName();
                //System.out.println(entryName);
                if (entryName.startsWith("java/io") || entryName.startsWith("java/nio")) {
                    if(entryName.endsWith(".class")){
                        count ++;
                        System.out.format(entryName.toString() + "\n");
                    }

                }
                entry = jarInputStream.getNextJarEntry();
            }
            jarInputStream.close();
            System.out.println(count);
            System.out.format("In .jar: # of .class files in java.io/java.nio packages: %d", count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
