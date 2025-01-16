package server.server_stream_rpc;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class HelloGrpcServer {

	/**
	 * Starts a gRPC server that listens on port 8080 and handles client requests.
	 *
	 * <p>This method initializes a gRPC server with the following configuration:
	 * - Binds to port 8080
	 * - Registers an instance of HelloGrpcServiceImpl to handle service requests
	 * - Starts the server and waits for termination
	 *
	 * @param args Command-line arguments (not used in this implementation)
	 * @throws IOException If there is an error starting the server
	 * @throws InterruptedException If the server is interrupted during termination
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// 클라이언트 요청을 수신하는데 사용할 포트 지정
		Server grpcServer = ServerBuilder
			.forPort(8080)
			.addService(new HelloGrpcServiceImpl())  // 서비스 구현 클래스의 인스턴스를 생성하여 .addService() 메서드에 전달
			.build();

		grpcServer.start();
		grpcServer.awaitTermination();
	}
}
