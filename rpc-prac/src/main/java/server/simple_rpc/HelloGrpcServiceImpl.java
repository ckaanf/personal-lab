package server.simple_rpc;

import com.example.grpc.HelloGrpc;
import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * 서버 동작 코드 (클라이언트에서 요청을 받음)
 */
@Slf4j  // 로그를 사용하기 위해 추가
public class HelloGrpcServiceImpl extends HelloGrpc.HelloImplBase {

	/**
	 * Handles a unary gRPC request to generate a personalized greeting.
	 *
	 * @param request The incoming HelloRequest containing the client's name
	 * @param responseObserver The stream observer used to send back the HelloResponse
	 */
	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
		log.info("=== Get Request");
		log.info("=== Request Data => [%s]".formatted(request));

		HelloResponse response = HelloResponse.newBuilder()
			.setGreetingMessage("Hello, %s".formatted(request.getName()))
			.setQuestionMessage("What do you do for fun?")
			.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
