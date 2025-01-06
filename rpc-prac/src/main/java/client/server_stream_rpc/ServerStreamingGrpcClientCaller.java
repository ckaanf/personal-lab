package client.server_stream_rpc;

import java.util.Iterator;

import com.example.grpc.HelloGrpc;
import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;

import client.GrpcClientCaller;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 클라이언트 동작 코드
 */
@Slf4j
public class ServerStreamingGrpcClientCaller implements GrpcClientCaller {

	private ManagedChannel channel;
	private HelloGrpc.HelloBlockingStub blockingStub;

	// 생성한 채널로 stub 생성
	public ServerStreamingGrpcClientCaller(ManagedChannel chl) {
		channel = chl;
		blockingStub = HelloGrpc.newBlockingStub(channel);
	}

	@Override
	public void send() {
		log.info(">>> Send Call");

		// 요청은 하나만 보내고, 여러 개의 응답을 받는다.
		Iterator<HelloResponse> helloResponseIterator = blockingStub.lotsOfReplies(HelloRequest.newBuilder()
			.setName("ckaanf")
			.setAge(32)
			.setMessage("Hello, Glad to meet you.")
			.build());

		helloResponseIterator.forEachRemaining(helloResponse -> {
			log.info(">>> Response Data => [%s]".formatted(helloResponse));
		});
	}
}