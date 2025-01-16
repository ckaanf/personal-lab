package client.simple_rpc;

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
public class SimpleGrpcClientCaller  implements GrpcClientCaller {

	private ManagedChannel channel;
	private HelloGrpc.HelloBlockingStub blockingStub;

	public SimpleGrpcClientCaller(ManagedChannel chl) {
		channel = chl;
		blockingStub = HelloGrpc.newBlockingStub(channel);
	}

	@Override
	public void send() {
		log.info(">>> Send Call");

		HelloResponse response = blockingStub.sayHello(HelloRequest.newBuilder()
			.setName("ckaanf")
			.setAge(31)
			.setMessage("gRPC :)")
			.build());

		log.info(">>> Response Data => [%s]".formatted(response));
	}
}