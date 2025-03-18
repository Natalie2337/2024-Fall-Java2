import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import com.google.gson.Gson;

public class Pokemon {
    public static void main(String[] args) throws IOException{
        String pokemonName = "pikachu";
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
            String json = response.toString();


            // System.out.println(response.toString());
        } else {
        System.out.println("Error: " + responseCode);
        }
    }
}
