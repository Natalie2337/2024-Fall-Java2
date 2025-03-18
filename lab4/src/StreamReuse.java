import java.util.function.Supplier;
import java.util.stream.Stream;

public class StreamReuse {
    public static void main(String[] args) {

        Stream<String> stream =
                Stream.of("CS", "209", "A")
                        .filter(s -> s.startsWith("C"));

        stream.anyMatch(s -> true);    // ok
        stream.forEach(System.out::println);   // exception

        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("CS", "209", "A")
                        .filter(s -> s.startsWith("C"));

        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().forEach(System.out::println);  // ok


        Stream<String> ss = Stream.of("CS", "209", "A");
        Supplier<Stream<String>> s = () -> ss; //same stream ss, cannot be reused
        s.get().forEach(System.out::println); // ok
        s.get().forEach(System.out::println); // exception
    }
}
