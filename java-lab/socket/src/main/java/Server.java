import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		// 인자가 있으면 해당 포트, 없으면 8080 사용
		int port = (args.length > 0) ? Integer.parseInt(args[0]) : 8080;

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("서버가 시작되었습니다. Port :: " + port);
			System.out.println("클라이언트 접속 대기 중..."); // 명시적 대기 로그

			Socket clientSocket = serverSocket.accept();
			System.out.println("클라이언트 연결 성공! (IP: " + clientSocket.getInetAddress() + ")"); // 접속 확인

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			for (int i = 0; i < 10000; i++) {
				out.println("x=" + i + ", y=20, z=30");
				if (i % 1000 == 0)
					System.out.println("데이터 전송 중... 현재 인덱스: " + i);
			}
			System.out.println("데이터 전송 루프 종료.");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}