import java.io.*;
import java.util.Base64;

class Student implements Serializable {
    //private static final long serialVersionUID = 1L;
    String name;
    //int age = 30;


    @Override
    public String toString(){
        return "Student name: " + name;
        //return "Student age:" + age;
        // return String.format("Student name %s, age %d", name, age);

    }
}

public class PersistenceDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Student student = new Student();
        //student.name = "alice";
        //student.age = 3;

//        String str = serialize(student);
//        System.out.println("Serialized: " + str);

        //Student str1 = (Student) deserialize(str);
        Student str1 = (Student) deserialize("rO0ABXNyABV0dXRvcmlhbC5sYWI2LlN0dWRlbnQAAAAAAAAAAQIAAUwABG5hbWV0ABJMamF2YS9sYW5nL1N0cmluZzt4cHQABWFsaWNl");
        System.out.println("Deserialized: " + str1);
    }

    public static String serialize(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Object deserialize(String s)
            throws IOException, ClassNotFoundException {

        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }
}
