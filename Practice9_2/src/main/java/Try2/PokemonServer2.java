package Try2;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PokemonServer2 {
    public static void main(String[] args) throws IOException {
        System.out.println("Waiting for clients to connect...");
        ServerSocket serverSocket = new ServerSocket(8888);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");
            Thread thread = new Thread(new ClientHandler(socket));
            thread.start();
        }
    }
}

class ClientHandler implements Runnable{ // 一定要是static class
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            while(true){
                InputStream inputStream = socket.getInputStream(); // inputstream输入
                byte[] buf = new byte[1024];
                int readlen = inputStream.read(buf);
                String pokemonName = new String(buf,0,readlen);
                if(pokemonName.equalsIgnoreCase("QUIT")){
                    socket.close();
                    System.out.println("Client quits.");
                    break;
                }
                System.out.println("Info of "+ pokemonName +" sent");

                // String pokemonName = "pikachu";
                String apiUrl = "https://pokeapi.co/api/v2/pokemon/" + pokemonName;
                URL url = new URL(apiUrl);
                // 创建一个URL连接对象，并将其转换为HttpURLConnection类型的对象。这个对象可以用来与指定的URL建立连接并进行数据传输。
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 设置HTTP请求的方法为GET方法
                conn.setRequestMethod("GET");
                // 链接上API endpoint
                conn.connect();

                int responseCode = conn.getResponseCode(); // 获取HTTP响应状态码
                String responseMessage = conn.getResponseMessage(); // 获取HTTP响应消息
                String contentEncoding = conn.getContentEncoding(); // 获取HTTP响应内容编码

                if(responseCode == HttpURLConnection.HTTP_OK){
                    // responseCode == HttpURLConnection.HTTP_OK 判断HTTP请求是否成功
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Gson gson = new Gson();
                    JsonObject pokemonData = gson.fromJson(response.toString(), JsonObject.class);
                    String name = pokemonData.get("name").getAsString();
                    double height = pokemonData.get("height").getAsDouble();
                    double weight = pokemonData.get("weight").getAsDouble();
                    var a = pokemonData.get("abilities").getAsJsonArray(); // 得到一个JsonArray

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    int len = name.length();
                    dataOutputStream.writeInt(len); // 先读取名字的长度
                    dataOutputStream.writeBytes(name);
                    dataOutputStream.writeDouble(height);
                    dataOutputStream.writeDouble(weight);

                    // 遍历JsonArray
                    List<String> l = new ArrayList<>();
                    for (JsonElement element:a) {
                        JsonObject obj = element.getAsJsonObject();
                        var a2 = obj.get("ability").getAsJsonObject();
                        var a3 = a2.get("name");
                        //System.out.println(a3);
                        l.add(a3.toString());
                        //dataOutputStream.writeUTF(a3.getAsString());
                        //System.out.println(a3.getAsString());
                    }
                    int length = l.size();
                    //System.out.println(length);
                    dataOutputStream.writeInt(length);

                    for (String str:l) {
                        dataOutputStream.writeUTF(str);
                    }


                    //System.out.println(l);

                    // JsonObject abilities = pokemonData.getAsJsonObject("abilities");
                    // JsonArray jsonArray = pokemonData.getAsJsonArray("abilities");


                    //System.out.println("Name: " + name);
                    //System.out.println("Height: " + height);
                    //System.out.println("Weight: " + weight);
                    //System.out.println(a);

                /*
                int cnt=l.size();
                dataOutputStream.writeInt(cnt);
                for (String str: l) {
                    int length = name.length();
                    dataOutputStream.writeInt(len); // 先读取名字的长度
                    dataOutputStream.writeBytes(name);
                }
                 */

                /*
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(l);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                 */

                }else {
                    System.out.println("Error: " + responseCode);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

