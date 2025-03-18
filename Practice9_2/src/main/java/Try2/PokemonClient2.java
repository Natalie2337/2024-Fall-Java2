package Try2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class PokemonClient2 {
    public static void main(String[] args) throws IOException {
        try{
            Socket socket = new Socket("localhost",8888);
            Scanner in = new Scanner(System.in);  // Scanner一个程序只能有有一个，必须要写在循环外面
            while(true){
                System.out.println("Enter the pokemon name:");

                String Name = in.next();
                if(Name.equalsIgnoreCase("QUIT")){
                    OutputStream outputStream = socket.getOutputStream();
                    byte[] nameBytes = Name.getBytes();
                    outputStream.write(nameBytes);
                    break;
                }
                OutputStream outputStream = socket.getOutputStream();// 对于客户端上传是outputstream
                byte[] nameBytes = Name.getBytes();
                outputStream.write(nameBytes);
                outputStream.flush(); //用了write()就一定要flush()

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int len = dataInputStream.readInt();
                //String name = Arrays.toString(dataInputStream.readNBytes(len)); 这个是错误的
                byte[] byteResult = dataInputStream.readNBytes(len);
                String name = new String(byteResult);
                double height = dataInputStream.readDouble();
                double weight = dataInputStream.readDouble();
                System.out.println("Name: "+name);
                System.out.println("Height: "+height);
                System.out.println("Weight: "+weight);


                int cnt = dataInputStream.readInt();
                List<String> l = new ArrayList<>();
                for (int i = 0; i < cnt; i++) {
                    String temp = dataInputStream.readUTF();
                    l.add(temp);
                }
                System.out.println("Ability"+l);
                /*
                int skip = len;
                System.out.println(cnt);
                List<String> list = new ArrayList<>();

                for (int i = 0; i < cnt; i++) {
                    int length = dataInputStream.readInt();
                    skip = skip+len;
                    byte[] buf = new byte[length];
                    dataInputStream.readNBytes(buf,0,length);
                    String ability = new String(buf);
                    list.add(ability);
                }

                System.out.println("Ability"+list);
                 */

                /*
                byte[] buf = new byte[1024];
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                List<String> list = (List<String>) objectInputStream.readObject();
                System.out.println(list);

                 */

            }
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
