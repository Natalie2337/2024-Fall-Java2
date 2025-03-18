import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.List;
import java.util.stream.Collectors;

public class Practice3 {

    public static List<Integer> func1(List<Integer> templist){
        Predicate<Integer> isEven = number -> number % 2 == 0;   //使用Predicate
        List<Integer> evenNumbers;
        evenNumbers = templist.stream().filter(isEven).collect(Collectors.toList());
        return  evenNumbers;
    }

    public static List<Integer> func2(List<Integer> templist){
        Predicate<Integer> isOdd = number -> number % 2 == 1;
        List<Integer> oddNumbers;
        oddNumbers = templist.stream().filter(isOdd).collect(Collectors.toList());
        return  oddNumbers;
    }

    public static boolean checkPrime(Integer number){
        if (number<1){
            return false;
        }else{
            for (int i = 2; i <= Math.sqrt(number); i++){
                if (number % i == 0){
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean checkPrimeBigger5(Integer number){
        if (number<5){
            return false;
        }else{
            for (int i = 7; i <= Math.sqrt(number); i++){
                if (number % i == 0){
                    return false;
                }
            }
            return true;
        }
    }

    public static List<Integer> func3(List<Integer> templist){
        Predicate<Integer> isPrime = Practice3::checkPrime;
        List<Integer> primeNumbers;
        primeNumbers= templist.stream().filter(isPrime).collect(Collectors.toList());
        return  primeNumbers;
    }

    public static List<Integer> func4(List<Integer> templist){
        Predicate<Integer> isPrimeBigger5 = Practice3::checkPrimeBigger5;
        List<Integer> primeNumbersBigger5;
        primeNumbersBigger5= templist.stream().filter(isPrimeBigger5).collect(Collectors.toList());
        return  primeNumbersBigger5;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in); //不确定这里是否能跨系调用

        while (true){
            System.out.println("""
                    Please input the function No:
                    1 - Get even numbers \s
                    2 - Get odd numbers \s
                    3 - Get prime numbers \s
                    4 - Get prime numbers that are bigger than 5 \s
                    0 - Quit \s
                    """);
            int funcNo = scanner.nextInt();
            if (funcNo == 0) break;
            System.out.println("Input size of the list: \n");
            int size = scanner.nextInt();
            ArrayList<Integer> num_list = new ArrayList<>();
            System.out.println("Input elements of the list: \n");
            for (int i = 0; i < size; i++) {
                int temp = scanner.nextInt();
                num_list.add(temp);
            }
            System.out.println("Filter results: \n");
            switch (funcNo) {
                case 1 -> System.out.println(func1(num_list));
                case 2 -> System.out.println(func2(num_list));
                case 3 -> System.out.println(func3(num_list));
                case 4 -> System.out.println(func4(num_list));
            }
        }

//        ArrayList<Integer> num_list = new ArrayList<>();
//
//
//        while(scanner.hasNextInt()){  //将数字读入ArrayList中
//            int number = scanner.nextInt();
//            num_list.add(number);
//            if (number==0){
//                num_list.add(number);
//                break;
//            }
//        }
//
//        System.out.println(num_list);
//        int len = num_list.size();
//        ArrayList<Integer> choose_func = new ArrayList<>();
//        ArrayList<Integer> arrlen0 = new ArrayList<>();
//        int head = 0;
//        choose_func.add(num_list.get(head));
//        head = head+1;
//        int temp = num_list.get(head);   //temp代表每一个小数组的长度
//        arrlen0.add(num_list.get(head));
//        head = head+temp;
//        while (head< len) {
//            head = head+1;
//            choose_func.add(num_list.get(head));
//
//            temp = num_list.get(head);    // 第一个数组的长度
//            arrlen0.add(num_list.get(head));
//            head = head + temp;
//        }
//
//        int size1 = choose_func.size();
//        int size2 = arrlen0.size();
//        for (int i = 0; i < size2; i++){
//            int head2 = 0;
//            List<Integer> templist = new ArrayList<>();
//            if (i==0){
//                head2 = 2;
//            }else{
//                for (int j = 1; j<=i; j++){
//                    head2 = head2+ arrlen0.get(j);
//                    head2 = head2 + 2;
//                }
//            }
//
//            for (int j = 0; j < arrlen0.get(i); j++){
//                templist.add(num_list.get(head2+j));
//            }
//
//            switch (choose_func.get(i)) {
//                case 1 -> System.out.println(func1(templist));
//                case 2 -> System.out.println(func2(templist));
//                case 3 -> System.out.println(func3(templist));
//                case 4 -> System.out.println(func4(templist));
//            }

//        }


    }

}
