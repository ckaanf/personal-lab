package server.client_stream_rpc;

import java.util.ArrayList;
import java.util.List;

import com.example.grpc.HelloGrpc;
import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * 서버 동작 코드 (클라이언트에서 요청을 받음)
 */
@Slf4j
public class HelloGrpcServiceImpl extends HelloGrpc.HelloImplBase {

	/**
	 * Implements a client streaming RPC method for processing multiple greeting requests.
	 *
	 * This method handles a stream of {@link HelloRequest} messages from the client and generates
	 * a single {@link HelloResponse} after all requests are processed. It accumulates names
	 * from incoming requests and creates a consolidated greeting message.
	 *
	 * @param responseObserver The observer used to send the final response back to the client
	 * @return A {@link StreamObserver} that processes incoming {@link HelloRequest} messages
	 *
	 * @see HelloRequest
	 * @see HelloResponse
	 */
	@Override
	public StreamObserver<HelloRequest> lotsOfGreetings(StreamObserver<HelloResponse> responseObserver) {
		return new StreamObserver<HelloRequest>() {

			List<String> nameList = new ArrayList<>();

			int i = 0;  // 강제 오류 발생 테스트를 위한 변수 (바로 오류를 발생시키지 않고 중간에 오류 발생시키기 위해 사용)

			/**
			 * Processes an incoming HelloRequest during client streaming.
			 *
			 * Adds the client's name to the name list and logs the name and age.
			 * Contains optional error generation mechanism for testing purposes.
			 *
			 * @param helloRequest The incoming client request containing name and age information
			 */
			@Override
			public void onNext(HelloRequest helloRequest) {
				nameList.add(helloRequest.getName());  // 응답 시 전달을 위해 요청 데이터의 name 값을 nameList에 담는다.
				log.info("=== name is %s [age: %d]".formatted(helloRequest.getName(), helloRequest.getAge()));

				/* 강제 오류 발생 테스트를 위한 코드 START (오류 테스트 시 주석 해제하여 사용) */
                /*i++;
                if (i == 2) {
                    try {
                        throw new Exception("Error Error Error!!");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }*/
				/* 강제 오류 발생 테스트를 위한 코드 END */
			}

			/**
			 * Handles errors that occur during the client streaming RPC process.
			 *
			 * @param t the {@code Throwable} representing the error that occurred during streaming
			 * @implNote Logs a warning message with the error details using SLF4J logging
			 */
			@Override
			public void onError(Throwable t) {
				log.warn("=== Warning => [%s]".formatted(t.getMessage()));
			}

			/**
			 * Completes the client streaming RPC by sending a final response.
			 *
			 * Constructs a {@code HelloResponse} with a greeting that includes all names
			 * received during the stream and a generic question message. After sending
			 * the response, marks the response stream as completed.
			 *
			 * @see HelloResponse
			 */
			@Override
			public void onCompleted() {
				responseObserver.onNext(HelloResponse.newBuilder()
					.setGreetingMessage("Hello, [%s]".formatted(String.join(",", nameList)))
					.setQuestionMessage("What do you do for fun?")
					.build());
				responseObserver.onCompleted();
			}
		};
	}
}
