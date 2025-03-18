import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileTypeParser {
    public static void main(String[] args) throws URISyntaxException {
        URI uri = FileTypeParser.class.getClassLoader().getResource("3").toURI();
        String filepath = Paths.get(uri).toString();

        try (FileInputStream fis = new FileInputStream(filepath); //这里输入师一个string
             InputStreamReader isr = new InputStreamReader(fis,"gb2312");
             BufferedReader bReader = new BufferedReader(isr);){

            byte[] readbytes = new byte[4];
            //由于一个byte是由8个bit组成，一个十六进制字符是由4个bit组成。因此存8个十六进制字符，需要4个byte
            int bytesRead = fis.read(readbytes);
            //fis.read(readbytes) 是一个阻塞方法，它尝试从输入流中读取字节，并将读取的字节数存储在readbytes数组中。返回值 bytesRead 表示实际读取的字节数。
            //需要注意的是，fis.read(bytes) 方法是阻塞的，它会一直等待直到有足够的字节可供读取或者达到流的末尾。如果流已经到达末尾，它会返回 -1。
            // bytesRead变量存储了实际读取的字节数。你可以使用这个值来确定读取了多少字节，并进行相应的处理。

            // 将byte转HEX
            String[] hexarr = new String[4];
            for (int i = 0; i < readbytes.length; i++) {
                hexarr[i] = String.format("%02X",readbytes[i]);
            }

            //判断文件类型
            StringBuilder sb = new StringBuilder();
            for (String i: hexarr){
                sb.append(i);
            }
            String head = sb.toString();

            System.out.println("Filename: " + "3");
            //System.out.println("File Header(Hex): " + hexarr); 这样打印出来的东西好像不太对
            System.out.println("File Header(Hex): " + Arrays.toString(hexarr));
            if (head.equalsIgnoreCase("89504e47")){
                System.out.println("File Type: " + "png");
            } else if (head.equalsIgnoreCase("504b0304")) {
                System.out.println("File Type: " + "zip or jar");
            } else if(head.equalsIgnoreCase("cafebabe")){
                System.out.println("File Type: " + "class");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
