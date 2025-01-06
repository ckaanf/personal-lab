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

	// Bidirectional Streaming RPC (양방향 스트리밍 RPC)
	@Override
	public StreamObserver<HelloRequest> bidiHello(StreamObserver<HelloResponse> responseObserver) {
		return new StreamObserver<HelloRequest>() {
			@Override
			public void onNext(HelloRequest helloRequest) {
				log.info("=== name is %s".formatted(helloRequest.getName()));

				responseObserver.onNext(HelloResponse.newBuilder()
					.setGreetingMessage("Hello, %s".formatted(helloRequest.getName()))
					.setQuestionMessage("What do you do for fun?")
					.build());
			}

			@Override
			public void onError(Throwable t) {
				log.warn("=== Warning => [%s]".formatted(t.getMessage()));
			}

			@Override
			public void onCompleted() {
				responseObserver.onCompleted();
			}
		};
	}
}
