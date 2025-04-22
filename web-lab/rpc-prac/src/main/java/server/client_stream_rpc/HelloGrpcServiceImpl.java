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

	// Client Streaming RPC (클라이언트 스트리밍 RPC)
	@Override
	public StreamObserver<HelloRequest> lotsOfGreetings(StreamObserver<HelloResponse> responseObserver) {
		return new StreamObserver<HelloRequest>() {

			List<String> nameList = new ArrayList<>();

			int i = 0;  // 강제 오류 발생 테스트를 위한 변수 (바로 오류를 발생시키지 않고 중간에 오류 발생시키기 위해 사용)

			// 요청 데이터에 대한 처리
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

			// 오류 처리
			@Override
			public void onError(Throwable t) {
				log.warn("=== Warning => [%s]".formatted(t.getMessage()));
			}

			// 응답 전달
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
