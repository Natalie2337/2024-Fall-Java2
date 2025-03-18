import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Practice4 {
    public static class City
    {
        private String name;
        private String state;
        private int population;

        public City(String name, String state, int population)
        {
            this.name = name;
            this.state = state;
            this.population = population;
        }

        public String getName()
        {
            return name;
        }

        public String getState()
        {
            return state;
        }

        //不确定是否需要加这个
        public int getPopulation()
        {
            return population;
        }

        @Override
        public String toString(){
            return "City{" + "name='" + name + "', state='" + state + "', population='" + population + '}';
        }

    }

    public static Stream<City> readCities(String filename) throws IOException
    {
        return Files.lines(Paths.get(filename))
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    public static void main(String[] args) throws IOException {

        Stream<City> cities = readCities(".idea/cities.txt");
        // Q1: count how many cities there are for each state
        // TODO: Map<String, Long> cityCountPerState = ...
        Map<String, Long> cityCountPerState = cities.collect(Collectors.groupingBy(City::getState,Collectors.counting()));
        System.out.println(cityCountPerState);


        cities = readCities(".idea/cities.txt");  //
        // Q2: count the total population for each state
        // TODO: Map<String, Integer> statePopulation = ...
        //用Collectors.summingInt()
        Map<String,Integer> statePopulation = cities.collect(Collectors.groupingBy(City::getState,Collectors.summingInt(City::getPopulation)));
        System.out.println(statePopulation);


        cities = readCities(".idea/cities.txt");
        // Q3: for each state, get the city with the longest name
        // TODO: Map<String, String> longestCityNameByState = ...

        //Collectors.mapping用于stream操作中对元素进行映射操作。它接将流中的每个元素通过函数映射为另一个元素，并返回映射后的结果。
        //Collectors.maxby(comparator)  根据指定的比较器返回Stream中的最大元素,返回的是Optional对象。
        // 因为 Map的值为String而非Optional<String>,因此需要在maxby最后.get()转成String类型的
        //Map<String,String> longestCityNameByState = cities.collect(Collectors.groupingBy(City::getState,Collectors.mapping(City::getName,Collectors.maxBy(Comparator.comparing(String::length)).orElse(" "))));
        Map<String, Optional<String>> longestCityNameByState = cities.collect(Collectors.groupingBy(City::getState,Collectors.mapping(City::getName,Collectors.maxBy(Comparator.comparing(String::length)))));
        //longestCityNameByState.values().forEach(element-> element.orElse(""));
        System.out.println(longestCityNameByState);


        cities = readCities(".idea/cities.txt");
        // Q4: for each state, get the set of cities with >500,000 population
        // TODO: Map<String, Set<City>> largeCitiesByState = ...
        // filter
        Map<String, Set<City>> largeCitiesByState = cities.filter(city -> (city.getPopulation()>500000)).collect(Collectors.groupingBy(City::getState,Collectors.toSet()));
        System.out.println(largeCitiesByState.toString());
    }
}
