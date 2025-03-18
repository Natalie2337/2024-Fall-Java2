import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;



public class Practice3Test {

    private final static String a1 = "1 4 2 3 4 5 2 5 2 3 4 5 6 3 4 2 3 4 5 4 4 4 5 6 7 4 1 1 0";

    private final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void test(){

        String res = "Please input the function No:\n" +
                "1 - Get even numbers\n" +
                "2 - Get odd numbers\n" +
                "3 - Get prime numbers\n" +
                "4 - Get prime numbers that are bigger than 5\n" +
                "0 - Quit\n" +
                "Input size of the list:\n" +
                "Input elements of the list:\n" +
                "Filter results:\n" +
                "[2, 4]\n" +
                "Please input the function No:\n" +
                "1 - Get even numbers\n" +
                "2 - Get odd numbers\n" +
                "3 - Get prime numbers\n" +
                "4 - Get prime numbers that are bigger than 5\n" +
                "0 - Quit\n" +
                "Input size of the list:\n" +
                "Input elements of the list:\n" +
                "Filter results:\n" +
                "[3, 5]\n" +
                "Please input the function No:\n" +
                "1 - Get even numbers\n" +
                "2 - Get odd numbers\n" +
                "3 - Get prime numbers\n" +
                "4 - Get prime numbers that are bigger than 5\n" +
                "0 - Quit\n" +
                "Input size of the list:\n" +
                "Input elements of the list:\n" +
                "Filter results:\n" +
                "[2, 3, 5]\n" +
                "Please input the function No:\n" +
                "1 - Get even numbers\n" +
                "2 - Get odd numbers\n" +
                "3 - Get prime numbers\n" +
                "4 - Get prime numbers that are bigger than 5\n" +
                "0 - Quit\n" +
                "Input size of the list:\n" +
                "Input elements of the list:\n" +
                "Filter results:\n" +
                "[7]\n" +
                "Please input the function No:\n" +
                "1 - Get even numbers\n" +
                "2 - Get odd numbers\n" +
                "3 - Get prime numbers\n" +
                "4 - Get prime numbers that are bigger than 5\n" +
                "0 - Quit\n" +
                "Input size of the list:\n" +
                "Input elements of the list:\n" +
                "Filter results:\n" +
                "[]\n" +
                "Please input the function No:\n" +
                "1 - Get even numbers\n" +
                "2 - Get odd numbers\n" +
                "3 - Get prime numbers\n" +
                "4 - Get prime numbers that are bigger than 5\n" +
                "0 - Quit"
                ;

        System.setIn(new ByteArrayInputStream(a1.getBytes()));
        Practice3.main(null);
        //demo.main(null);
        assertEquals(res.replace("\n", "")
                .replace("\r", ""),outContent.toString().replace("\n", "")
                .replace("\r", ""));
    }
}
