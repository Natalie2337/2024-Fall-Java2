import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class FindInnerClass {
    public static void main(String[] args) throws IOException {
        //找到jdk的路径
        //String jdkDirectory = System.getProperty("java.home");
        //找到当下的工作路径
        String directory = System.getProperty("user.dir");

        //rt.jar的路径。
        Path path = Paths.get(directory, "rt.jar");
        //src.zip的路径
        Path path2 = Paths.get(directory, "src.zip");

        try {
            // 使用JarInputStream
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(path.toString()));
            JarEntry entry = jarInputStream.getNextJarEntry();
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path2.toString()));
            ZipEntry entry2 = zipInputStream.getNextEntry();
            //int count = 0;

            int totalClassFiles = 0;
            int totalJavaFiles = 0;
            int JavaFileCorrespondClass = 0;
            int JavaWithoutClass = 0;
            int ClassWithoutJava = 0;
            int innerClassFiles = 0;
            List<String> notinnerclassList = new ArrayList<>();
            List<String> javaList = new ArrayList<>();
            List<String> javawithoutclassList = new ArrayList<>();
            List<String> classwithoutjavaList = new ArrayList<>();

            while (entry != null) {
                String entryName = entry.getName();
                //System.out.println(entryName);

                if (entryName.startsWith("java/io") || entryName.startsWith("java/nio")) {

                    if(entryName.endsWith(".class")){
                        totalClassFiles ++;
                        //System.out.format(directory_path.toString() + "\n");
                        //如果条目名称中包含Inner Class标识符 '$'，则将其计数
                        if(entryName.contains("$")){
                            innerClassFiles ++;
                        }else{
                            notinnerclassList.add(entryName);
                        }
                    }
                    /*
                    if(entryName.endsWith(".java")){
                        totalJavaFiles ++;
                    }
                    //注意！.jar文件中不包含.java文件。
                    .jar文件是Java的可执行文件，它是将Java源代码编译成的字节码文件进行打包的一种格式。
                    .java文件是Java源代码文件，需要经过编译才能生成字节码文件（.class文件）。
                    所以在一个.jar文件中，只包含已经编译好的字节码文件（.class文件），而不包含源代码文件（.java文件）。
                    */
                }
                entry = jarInputStream.getNextJarEntry();
            }
            jarInputStream.close();

            while (entry2 != null) {
                String entryName = entry2.getName();
                //System.out.println(entryName);

                if (entryName.startsWith("java/io") || entryName.startsWith("java/nio")) {
                    if(entryName.endsWith(".java")){
                        totalJavaFiles ++;
                        javaList.add(entryName);
                        String temp = entryName.replace(".java",".class"); //把.java换成.class
                        //System.out.println("temp: " + temp);
                        boolean classexist = false;
                        for (String name: notinnerclassList) {
                            //System.out.println("name" + name);
                            if (temp.equals(name)){
                                JavaFileCorrespondClass ++;
                                classexist = true;
                                break;
                            }
                        }
                        if(!classexist){
                            JavaWithoutClass ++;
                            javawithoutclassList.add(entryName);
                        }
                    }
                }
                entry2 = zipInputStream.getNextEntry();
            }
            zipInputStream.close();

            System.out.format("\n # total .class files in rt.jar: %d", totalClassFiles);
            System.out.format("\n # total .java files in src.zip: %d", totalJavaFiles);
            System.out.format("\n # of .java files with corresponding .class: %d", JavaFileCorrespondClass);
            System.out.format("\n # of .class files for inner classes: %d", innerClassFiles);
            System.out.println("\n");

            // .java without class:
            JavaWithoutClass = javawithoutclassList.size();
            System.out.format("\n # of .java without its .class: %d", JavaWithoutClass);
            System.out.println("\n");
            for (String s: javawithoutclassList) {
                System.out.println(s.toString());
            }

            // .class without java

            for (String s: notinnerclassList) {
                boolean javaexist = false;
                String temp = s.replace(".class",".java");
                for (String t: javaList) {
                    if(temp.equals(t)){
                        javaexist = true;
                        break;
                    }
                }
                if(!javaexist){
                    ClassWithoutJava++;
                    classwithoutjavaList.add(s);
                }

            }

            System.out.format("\n # of .class without its .java: %d", ClassWithoutJava);
            System.out.println("\n");
            for (String s: classwithoutjavaList) {
                System.out.println(s.toString());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

