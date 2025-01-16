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

	/**
	 * Constructs a SimpleGrpcClientCaller with the specified gRPC channel.
	 *
	 * @param chl The ManagedChannel used for establishing gRPC communication
	 * @throws IllegalArgumentException if the provided channel is null
	 */
	public SimpleGrpcClientCaller(ManagedChannel chl) {
		channel = chl;
		blockingStub = HelloGrpc.newBlockingStub(channel);
	}

	/**
	 * Sends a predefined gRPC Hello request to the server and logs the response.
	 *
	 * This method constructs a HelloRequest with hardcoded values and uses the blocking stub
	 * to make a synchronous gRPC service call. It logs the initiation of the call and the
	 * received response.
	 *
	 * @return void
	 * @throws RuntimeException if the gRPC service call encounters an error
	 */
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