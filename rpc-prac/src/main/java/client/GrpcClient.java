package client;

import static client.StreamType.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
	/**
	 * Main entry point for the gRPC client application.
	 *
	 * Establishes a gRPC channel to a local server and sends a request using a simple stream type.
	 *
	 * @param args Command-line arguments (not used in this implementation)
	 * @throws InterruptedException If the client execution is interrupted during communication
	 *
	 * @see ManagedChannelBuilder
	 * @see GrpcClientCaller
	 */
	public static void main(String[] args) throws InterruptedException {
		// 스텁에 대한 gRPC 채널을 생성하고 연결하려는 서버 주소와 포트를 지정
		// 채널을 생성하려면 ManagedChannelBuilder를 사용합니다.
		ManagedChannel channel = ManagedChannelBuilder
			.forAddress("localhost", 8080)
			.usePlaintext()
			.build();

		GrpcClientCaller helloGrpcClientCaller = SIMPLE.createCaller(channel);
		helloGrpcClientCaller.send();
	}
}
