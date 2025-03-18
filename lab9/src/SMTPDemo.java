import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class SMTPDemo {

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("smtp.qq.com", 25);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("HELO localhost");
        // 220 means the SMTP server is ready to proceed with the next command.
        System.out.println("Response: " + in.readLine());
        // 250 means everything went well and your message was delivered to the recipient server.
        System.out.println("Response: " + in.readLine());


        out.println("auth login");
        // 334 means input Base64-encoded mail address
        System.out.println("Response: " + in.readLine());
        out.println(Base64.getEncoder().encodeToString("2970939403@qq.com".getBytes()));
        // 334 means input Base64-encoded password
        System.out.println("Response: " + in.readLine());
        out.println(Base64.getEncoder().encodeToString("hyh20011225".getBytes()));
        // 235: Authentication succeeds
        System.out.println("Response: " + in.readLine());

        out.println("MAIL FROM: yourmail@sustech.edu.cn");
        // 250 means everything went well and your message was delivered to the recipient server.
        System.out.println("Response: " + in.readLine());
        // send to this address
        out.println("RCPT To: yourmail@sustech.edu.cn");
        System.out.println("Response: " + in.readLine());

        // mail information
        out.println("DATA");
        out.println("From: \"Alice\" <alice@qq.com>");
        out.println("To: \"Bob\" <bob@qq.com>");
        out.println("Date: Tue, 15 Jan 2008 16:11:11-0500");
        out.println("Subject: Just wanted to say hi");
        out.println();
        out.println("This is the message body.");
        out.println(".");
        out.println("QUIT");

        String str;
        // 354: start mail input
        // 250 OK
        while((str = in.readLine())!=null){
            System.out.println(str);
        }

        // clean up
        out.close();
        in.close();
    }


}
