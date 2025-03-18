import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

public class tryFlatMap {
    public static void main(String[] args) {

        String[][] array = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};

        // convert  array to a stream
        Stream<String[]> stream = Arrays.stream(array);

        // array to a stream [same result]
        Stream<String[]> array1 = Stream.of(array);

        // [a, b, c, d, e, f]
        String[] objects = Arrays.stream(array)
                .flatMap(Stream::of)
                .toArray(String[]::new);

        List<String> collect = Arrays.stream(array)
                .flatMap(Stream::of)
                .filter(x -> !x.equals("a"))
                .toList();

        collect.forEach(System.out::println);

    }

}
