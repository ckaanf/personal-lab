package server.bidirectional_stream_rpc;

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
	 * Implements a bidirectional streaming RPC method for hello service.
	 *
	 * This method establishes a bidirectional stream where the server can send multiple responses
	 * for each client request. It allows continuous communication between client and server.
	 *
	 * @param responseObserver The observer used to send responses back to the client
	 * @return A StreamObserver that handles incoming client requests
	 *
	 * @see StreamObserver
	 */
	@Override
	public StreamObserver<HelloRequest> bidiHello(StreamObserver<HelloResponse> responseObserver) {
		return new StreamObserver<HelloRequest>() {
			/**
			 * Handles the next incoming request in a bidirectional streaming RPC interaction.
			 *
			 * This method is called for each request received from the client. It logs the name
			 * from the request and sends back a personalized greeting response with a follow-up question.
			 *
			 * @param helloRequest The incoming request containing a client's name
			 * @see StreamObserver
			 */
			@Override
			public void onNext(HelloRequest helloRequest) {
				log.info("=== name is %s".formatted(helloRequest.getName()));

				responseObserver.onNext(HelloResponse.newBuilder()
					.setGreetingMessage("Hello, %s".formatted(helloRequest.getName()))
					.setQuestionMessage("What do you do for fun?")
					.build());
			}

			/**
			 * Handles errors that occur during the bidirectional streaming RPC communication.
			 *
			 * @param t the {@link Throwable} representing the error that occurred during stream processing
			 * @implNote Logs a warning message with the error's details using SLF4J logging
			 */
			@Override
			public void onError(Throwable t) {
				log.warn("=== Warning => [%s]".formatted(t.getMessage()));
			}

			/**
			 * Signals the completion of the bidirectional streaming RPC request.
			 *
			 * This method is called when all requests from the client have been processed,
			 * and it indicates that no more requests will be sent. It propagates the completion
			 * signal to the response observer, effectively closing the streaming communication.
			 *
			 * @see StreamObserver
			 */
			@Override
			public void onCompleted() {
				responseObserver.onCompleted();
			}
		};
	}
}
