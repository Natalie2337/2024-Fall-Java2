import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AssertionsDemo {

    private final Calculator calculator = new Calculator();

    private final Person person = new Person("Jane", "Doe");

    @Test
    void standardAssertions() {
        Assertions.assertEquals(2, calculator.add(1, 1));
        Assertions.assertEquals(4, calculator.multiply(2, 2),
                "The optional failure message is now the last parameter");
        Assertions.assertTrue('a' < 'b', () -> "Assertion messages can be lazily evaluated -- "
                + "to avoid constructing complex messages unnecessarily.");
    }

    @Test
    void groupedAssertions() {
        // In a grouped assertion all assertions are executed, and all
        // failures will be reported together.
        Assertions.assertAll("person",
                () -> Assertions.assertEquals("Jane", person.getFirstName()),
                () -> Assertions.assertEquals("Doe", person.getLastName())
        );
    }

    @Test
    void dependentAssertions() {
        // Within a code block, if an assertion fails the
        // subsequent code in the same block will be skipped.
        Assertions.assertAll("properties",
                () -> {
                    String firstName = person.getFirstName();
                    Assertions.assertNotNull(firstName);

                    // Executed only if the previous assertion is valid.
                    Assertions.assertAll("first name",
                            () -> Assertions.assertTrue(firstName.startsWith("J")),
                            () -> Assertions.assertTrue(firstName.endsWith("e"))
                    );
                },
                () -> {
                    // Grouped assertion, so processed independently
                    // of results of first name assertions.
                    String lastName = person.getLastName();
                    Assertions.assertNotNull(lastName);

                    // Executed only if the previous assertion is valid.
                    Assertions.assertAll("last name",
                            () -> Assertions.assertTrue(lastName.startsWith("D")),
                            () -> Assertions.assertTrue(lastName.endsWith("e"))
                    );
                }
        );
    }

    @Test
    void exceptionTesting() {
        Exception exception = Assertions.assertThrows(ArithmeticException.class, () ->
                calculator.divide(1, 0));
        Assertions.assertEquals("/ by zero", exception.getMessage());
    }

    @Test
    void timeoutNotExceeded() {
        // The following assertion succeeds.
        Assertions.assertTimeout(ofMinutes(2), () -> {
            // Perform task that takes less than 2 minutes.
        });
    }

    @Test
    void timeoutNotExceededWithResult() {
        // The following assertion succeeds, and returns the supplied object.
        String actualResult = Assertions.assertTimeout(ofMinutes(2), () -> {
            return "a result";
        });
        Assertions.assertEquals("a result", actualResult);
    }

    @Test
    void timeoutNotExceededWithMethod() {
        // The following assertion invokes a method reference and returns an object.
        String actualGreeting = Assertions.assertTimeout(ofMinutes(2), AssertionsDemo::greeting);
        Assertions.assertEquals("Hello, World!", actualGreeting);
    }

    @Test
    void timeoutExceeded() {
        // The following assertion fails with an error message similar to:
        // execution exceeded timeout of 10 ms by 91 ms
        Assertions.assertTimeout(ofMillis(10), () -> {
            // Simulate task that takes more than 10 ms.
            Thread.sleep(100);
        });
    }

    @Test
    void timeoutExceededWithPreemptiveTermination() {
        // The following assertion fails with an error message similar to:
        // execution timed out after 10 ms

        Assertions.assertTimeoutPreemptively(ofMillis(10), () -> {
            // Simulate task that takes more than 10 ms.
            new CountDownLatch(1).await();
        });
        //The difference between assertTimeout and assertTimoutPreemptively assertion is that:
        // the assertTimeout will continue after the operation times out, and the actual execution time of
        // the operation will be recorded in the final test report(For example: execution exceeded timeout of 10 ms by 91 ms).
        // The latter end immediately after the specified time is reached, and only the operation timeout is reflected in the final report,
        // but the actual execution time is not included(For example: execution timed out after 10 ms, no actual execution time).
    }

    private static String greeting() {
        return "Hello, World!";
    }

}
