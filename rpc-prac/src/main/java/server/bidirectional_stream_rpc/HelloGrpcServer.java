package server.bidirectional_stream_rpc;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class HelloGrpcServer {

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
