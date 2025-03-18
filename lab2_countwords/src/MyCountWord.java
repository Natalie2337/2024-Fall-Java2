import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

public class MyCountWord {
    public static void main(String[] args) {
        try{
            File file = new File("D:\\IDEA_project\\Fall2023\\lab2_countwords\\src\\alice.txt");
            Scanner scanner = new Scanner(file);

            ArrayList<String> wordlist = new ArrayList<>();  //利用ArrayList将单词存储
            while(scanner.hasNext()){
                // scanner.hasNext() 以空格为分隔读入
                String word = scanner.next();
                wordlist.add(word.toLowerCase()); // 全部换成小写，把开头首字母大写也算同一个单词
            }

            // 利用HashMap来统计每个词的出现次数
            HashMap<String,Integer> wordfrequncymap = new HashMap<>();

            // 把ArrayList中的放入HashMap中。并让value为出现次数
            for(String word : wordlist){
                // map.getOrDefault(): 第一个参数是键（key），第二个参数是默认值0。如果 map 中包含指定的键，则返回与该键关联的值；如果不包含指定的键，则返回默认值。
                int count = wordfrequncymap.getOrDefault(word, 0);
                count = count+1;
                wordfrequncymap.put(word,count);
            }

            // 利用TreeMap进行降序排列.TreeMap对键进行自然排序(升序排序)，因此可以自定义Comparator接口来进行降序排序
            // 假设我们要对value进行排序，那么我们可以将其转为List再利用Collections进行排序
            Comparator<Integer> mycomparator = new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2-o1;
                }
            };

            TreeMap<Integer,String> topfrequencymap = new TreeMap<>(mycomparator);
        /*
            使用entrySet方法获取键值对的Set视图
            Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

            // 转换为List，方便排序
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(entrySet);

            // 使用Collections.sort方法按键进行排序
            Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                    return entry1.getKey().compareTo(entry2.getKey());
                }
            });
        */
            // 遍历HashMap中的映射(键值对)，并存入TreeMap中：
            for (Map.Entry<String,Integer> entry: wordfrequncymap.entrySet()){
                String hashmapkey = entry.getKey();
                Integer hashmapvalue = entry.getValue();
                topfrequencymap.put(hashmapvalue,hashmapkey);
            }

            // 输出前5个
            int count=0;
            System.out.println("Word : Count");
            for (Map.Entry<Integer,String> entry: topfrequencymap.entrySet()){
                if (count<5){
                    String treemapvalue = entry.getValue();
                    Integer treemapkey = entry.getKey();
                    System.out.println(treemapvalue+" : "+treemapkey);
                }
                count = count+1;
            }

        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }
}
