import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        // 외부에서 포트 주입받음
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : 8080;

        System.out.println("포트 " + port + "로 연결 시도 중...");

        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("서버 접속 완료!");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String response;
            int count = 0;
            // 서버가 연결을 끊을 때까지 읽기
            while ((response = in.readLine()) != null) {
                // 데이터 수신 (병목 확인을 위해 출력은 최소화하거나 제거)
                count++;
            }
            System.out.println("서버가 연결을 정상적으로 종료했습니다. 총 수신 데이터: " + count);

        } catch (IOException e) {
            System.err.println("오류 발생: " + e.getMessage());
        }
    }
}