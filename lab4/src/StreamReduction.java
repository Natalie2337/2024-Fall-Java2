import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student {
    String name;
    int age;

    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name;
    }
}

class College {
    String collegeName;
    List<Student> students = new ArrayList<>();

    College(String name) {
        this.collegeName = name;
    }
}

public class StreamReduction {
    public static void main(String[] args) {
        List<Student> students =
                Arrays.asList(
                        new Student("Zhang San", 17),
                        new Student("Li si", 21),
                        new Student("Wang Wu", 21),
                        new Student("Liu Liu", 19));

        /************************************Collect******************************************/
        /**Collect is an extremely useful terminal operation to transform the elements of the
         * stream into a different kind of result, e.g. a List, Set or Map. Collect accepts a
         * Collector which consists of four different operations: a supplier, an accumulator,
         * a combiner and a finisher. It is a little complicated.
         * But Java 8 supports various built-in collectors via the Collectors class.
         * So for the most common operations you don't have to implement a collector yourself. */

        //construct a list from the elements of a stream
        //use Collectors.toSet() when you need a set
        List<Student> filteredStudentsList = students
                .stream()
                .filter(s -> s.name.startsWith("L"))
                .collect(Collectors.toList());

        System.out.println(filteredStudentsList);
        //output:
        // [Li si, Liu Liu]

        //groups all students by age
        Map<Integer, List<Student>> studentsByAge = students
                .stream()
                .collect(Collectors.groupingBy(s -> s.age));

        studentsByAge
                .forEach((age, s) -> System.out.format("age %s: %s\n", age, s));
        //output:
        //age 17: [Zhang San]
        //age 19: [Liu Liu]
        //age 21: [Li si, Wang Wu]

        //create aggregations on the elements of the stream,
        // e.g. calculate the average age of all students:
        Double averageAge = students
                .stream()
                .collect(Collectors.averagingInt(p -> p.age));
        System.out.println(averageAge);
        //output:
        // 19.5

        //the summarizing collectors return a special built-in summary statistics object.
        // you can get count,sum, min, max,  average age of the students.
        IntSummaryStatistics ageSummary = students
                .stream()
                .collect(Collectors.summarizingInt(s -> s.age));

        System.out.println(ageSummary);
        //output:
        // IntSummaryStatistics{count=4, sum=78, min=17, average=19.500000, max=21}

        //The join collector accepts a delimiter as well as an optional prefix and suffix.
        //The example joins all students into a single string:
        String phrase = students
                .stream()
                .filter(s -> s.age >= 18)
                .map(s -> s.name)
                .collect(Collectors.joining(" and ", "In China ", " are of legal age."));
        System.out.println(phrase);
        //output:
        // In China Li si and Wang Wu and Liu Liu are of legal age.

        //In order to transform the stream elements into a map,
        //you have to specify how both the keys and the values should be mapped.
        // Keep in mind that the mapped keys must be unique, otherwise an IllegalStateException is thrown.
        // You can optionally pass a merge function as an additional parameter to bypass the exception.
        Map<Integer, String> map = students
                .stream()
                .collect(Collectors.toMap(
                        s -> s.age,
                        s -> s.name,
                        (name1, name2) -> name1 + "|" + name2));
        System.out.println(map);
        //output:
        // {17=Zhang San, 19=Liu Liu, 21=Li si|Wang Wu}

        //All above example use the built-in collectors.
        /**The following example transforms all students of the stream into a single string consisting of all
         * names in upper letters separated by the | pipe character.
         * In order to achieve this goal, we have to create a new collector via Collector.of().
         * We have to pass the four ingredients of a collector: a supplier, an accumulator, a combiner and a finisher.
         *
         * Since strings in Java are immutable, we need a helper class like StringJoiner
         * to let the collector construct our string. The supplier initially constructs
         * such a StringJoiner with the appropriate delimiter. The accumulator is used
         * to add each person's upper-cased name to the StringJoiner. The combiner knows
         * how to merge two StringJoiners into one. In the last step the finisher constructs
         * the desired String from the StringJoiner.*/
        Collector<Student, StringJoiner, String> studentsNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "), // supplier
                        (j, s) -> j.add(s.name.toUpperCase()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String names = students
                .stream()
                .collect(studentsNameCollector);

        System.out.println(names);
        //output:
        //ZHANG SAN | LI SI | WANG WU | LIU LIU

        /************************************flatMap******************************************/
        /** map: transform the objects of a stream into another type of objects
         * flatmap: transform one object into multiple others or none at all*/

        List<College> collegeList = new ArrayList<>();

        // create Colleges
        IntStream
                .range(1, 3)
                .forEach(i -> collegeList.add(new College("College" + i)));

        //add students
        collegeList.forEach(f -> IntStream
                .range(0, 2)
                .forEach(i -> {
                    int collegeNumber = Integer.parseInt(f.collegeName.substring(7));
                    f.students.add(students.get((collegeNumber - 1) * 2 + i));
                }));

        //Now we have a list of two colleges each consisting of two students.
        // FlatMap accepts a function which has to return a stream of objects.
        // So in order to resolve the bar objects of each foo, we just pass
        // the appropriate function:
        collegeList.stream()
                .flatMap(f -> f.students.stream())
                .forEach(b -> System.out.println(b.name));

        //output:
        //Zhang San
        //Li si
        //Wang Wu
        //Liu Liu

        /************************************reduce******************************************/

        /** The reduction operation combines all elements of the stream into a single result.
         * Java 8 supports three different kind of reduce methods.*/

        //The first one reduces a stream of elements to exactly one element of the stream.
        //The reduce method accepts a BinaryOperator accumulator function. That's actually
        // a BiFunction where both operands share the same type, in that case Student.
        // BiFunctions are like Function but accept two arguments. The example function
        // compares both students ages in order to return the student with the maximum age.
        students
                .stream()
                .reduce((s1, s2) -> s1.age > s2.age ? s1 : s2)
                .ifPresent(System.out::println); //A student with the maximum age
        //Output:
        //Wang Wu

        //The second reduce method accepts both an identity value and a BinaryOperator accumulator.
        // This method can be utilized to construct a new Student with the aggregated names and ages
        // from all other students in the stream:
        Student result =
                students
                        .stream()
                        .reduce(new Student("", 0), (s1, s2) -> {
                            s1.age += s2.age;
                            s1.name = s1.name + "|" +s2.name;
                            return s1;
                        });

        System.out.format("name=%s; age=%s\n", result.name, result.age);
        //Output:
        // name=|Zhang San|Li si|Wang Wu|Liu Liu; age=78

        //The third reduce method accepts three parameters: an identity value,
        // a BiFunction accumulator and a combiner function of type BinaryOperator.
        // Since the identity values type is not restricted to the Person type, we
        // can utilize this reduction to determine the sum of ages from all students:
        Integer ageSum = students
                .stream()
                .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

        System.out.println(ageSum);
        //Output:
        // 78


    }
}