import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExceptionDemo {
    public static void main(String[] args) throws FileNotFoundException {

        try
        {
            //InputStream in = new FileInputStream("nonexist-file");
            InputStream in = new FileInputStream("bytewriter_output.txt");
            String s = null;
            s.length();

            System.out.println("End of try.");
        }
        catch (IOException e)
        {
            System.out.println("Catch begins.");
            InputStream in = new FileInputStream("nonexist-file");
            System.out.println("End of catch");
        }
        finally
        {
            //InputStream in = new FileInputStream("nonexist-file");
            System.out.println("Finally.");
        }

        System.out.println("After finally.");

    }
}