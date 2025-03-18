import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.lang.annotation.*;

public class DataValidator {

    public static boolean validate(Object obj) throws IllegalAccessException {    //实现validation的逻辑
        // TODO
        //Class<User> uclass = User.class;
        //Field[] fields = uclass.getFields();
        //Field[] fields = obj.getClass().getDeclaredFields();

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Object username;
        Object password;
        boolean valid_username = false;
        boolean valid_password = false;

        Field field0 = fields[0];
        Field field1 = fields[1];

        //for(Field field:fields) {
            //System.out.println(field.getName());
        //}

        if(field0.getName().equals("username")){
            field0.setAccessible(true);         // 反射中打破封装，使可以访问和修改类中的私有属性
            username = field0.get(obj);         // 反射中用于获取指定对象obj中字段field的值。通过反射获取对象的字段值，可以用来进行一些自定义的验证逻辑
            if(username instanceof String){           // 判断是否是String，才能进行后面的操作，不能直接用Object类型
                // Annotation annotation_l = field.getAnnotation(MinLength.class);
                MinLength annotation_l = field0.getAnnotation(MinLength.class);
                int minLen = annotation_l.min();
                boolean a = checkLength((String) username,minLen);

                CustomValidation annotation_c = field0.getAnnotation(CustomValidation.class);  // 直接这样是错误的
                Rule rule = annotation_c.rule();

                boolean temp = true;
                temp = temp && checkRules(rule,obj);

                //CustomValidations annotaion_c = field0.getAnnotation(CustomValidations.class);
                //CustomValidation[] annotation_list = annotaion_c.value();

                if(a && temp) valid_username = true;
            }
        }

        if(field1.getName().equals("password")){
            field1.setAccessible(true);
            password = field1.get(obj);
            if(password instanceof String){

                MinLength annotation_l = field1.getAnnotation(MinLength.class);
                // int minLen = annotation_l.min(8);
                int minLen = annotation_l.min();
                boolean a = checkLength((String) password,minLen);

                CustomValidations annotaion_c = field1.getAnnotation(CustomValidations.class);
                CustomValidation[] annotation_list = annotaion_c.value();

                boolean temp = true;

                for (CustomValidation c: annotation_list) {
                    Rule rule = c.rule();
                    temp = temp && checkRules(rule,obj);
                }

                if(a && temp) valid_password = true;
            }
        }

        return (valid_username && valid_password);
    }


    public static boolean checkLength(String str, int minLen){
        if(str.length()<minLen){
            System.out.format("Validation failed for field *username*: should have a minimum length of %d \n",minLen);
            return false;
        }else{
            return true;
        }
    }

    public static boolean checkRules(Rule rule, Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Field username_field = fields[0];
        Field password_field = fields[1];

        username_field.setAccessible(true);
        password_field.setAccessible(true);
        /*
        for(Field field:fields){
            if (field.getName().equals("username")){
                username_field = field;
                username_field.setAccessible(true);
            }
            if(field.getName().equals("username")){
                password_field = field;
                password_field.setAccessible(true);
            }
        }

        assert username_field!=null;
        assert password_field!=null;
         */
        String username = (String) username_field.get(obj);
        String password = (String) password_field.get(obj);

        switch(rule){
            case ALL_LOWERCASE:
                String lowerCase = username.toLowerCase();
                if(username.equals(lowerCase)){
                    return true;
                }else{
                    System.out.println("Validation failed for field *username*: should be all lowercase \n");
                    return false;
                }
                //return username.equals(lowerCase);

            case NO_USERNAME:
                if(password.contains(username)){
                    System.out.println("Validation failed for field *password*: should not contain username \n");
                    return false;
                }else{
                    return true;
                }
                //return (!password.contains(username));

            case HAS_BOTH_DIGITS_AND_LETTERS:
                boolean hasDigits = false;
                boolean hasLetters = false;
                char[] chars = password.toCharArray();
                for (char c:chars) {
                    if(Character.isDigit(c)) hasDigits=true;
                    if(Character.isAlphabetic(c)) hasLetters=true;
                }
                if(hasDigits && hasLetters){
                    return true;
                }else{
                    System.out.println("Validation failed for field *password*: should have both letters and digits \n");
                    return false;
                }
                //return (hasDigits && hasLetters);

            default:
                return false;
        }
    }



    public static void main(String[] args) throws IllegalAccessException {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("Username: ");
            String username = sc.next();
            System.out.print("Password: ");
            String pwd = sc.next();
            User user = new User(username,pwd);
            //validationLogic();

            if(validate(user)){
                System.out.println("Success!");
                break;
            }

        }
    }
}
