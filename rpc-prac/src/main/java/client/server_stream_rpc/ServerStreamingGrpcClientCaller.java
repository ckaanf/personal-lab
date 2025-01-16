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

	/**
	 * Constructs a new ServerStreamingGrpcClientCaller with the specified gRPC channel.
	 *
	 * @param chl The ManagedChannel used to establish the connection to the gRPC server
	 * @throws IllegalArgumentException if the provided channel is null
	 */
	public ServerStreamingGrpcClientCaller(ManagedChannel chl) {
		channel = chl;
		blockingStub = HelloGrpc.newBlockingStub(channel);
	}

	/**
	 * Sends a single server-streaming gRPC request and processes multiple responses.
	 *
	 * This method demonstrates a server-streaming RPC call where:
	 * - A single {@code HelloRequest} is sent to the server
	 * - Multiple {@code HelloResponse} messages are received and logged
	 *
	 * The request includes a predefined name, age, and message.
	 * Each response from the server is logged using the iterator.
	 *
	 * @see HelloGrpc
	 * @see HelloRequest
	 * @see HelloResponse
	 */
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