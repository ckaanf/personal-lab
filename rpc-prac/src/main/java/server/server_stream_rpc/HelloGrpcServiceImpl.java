package server.server_stream_rpc;

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

	private String[] questionMessages = {
		"a question",
		"b question",
		"c question",
		"d question",
		"e question"
	};

	// Server Streaming RPC (서버 스트리밍 RPC)
	@Override
	public void lotsOfReplies(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
		log.info("=== Get Request");
		log.info("=== Request Data => [%s]".formatted(request));

		for (String questionMessage: questionMessages) {
			HelloResponse response = HelloResponse.newBuilder()
				.setGreetingMessage("Hello, %s".formatted(request.getName()))
				.setQuestionMessage(questionMessage)
				.build();

			responseObserver.onNext(response);
		}

		responseObserver.onCompleted();
	}
}
