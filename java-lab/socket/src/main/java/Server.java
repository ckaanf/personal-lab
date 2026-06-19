import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("서버 대기 중...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("연결됨!");

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("Hello, Client!");

        clientSocket.close();
        serverSocket.close();
    }
}