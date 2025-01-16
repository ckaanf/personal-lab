package client.bidirectional_stream_rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.example.grpc.HelloGrpc;
import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;

import client.GrpcClientCaller;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
/**
 * 클라이언트 동작 코드
 */
@Slf4j  // 로그를 사용하기 위해 추가
public class BidirectionalStreamingGrpcClientCaller implements GrpcClientCaller {

	private ManagedChannel channel;
	private HelloGrpc.HelloStub asyncStub;

	private String[] names = {
		"Alice",
		"Billy",
		"Cindy",
		"Danny",
		"Fiona"
	};

	private int minAge = 1;
	private int maxAge = 100;

	/**
	 * Constructs a new BidirectionalStreamingGrpcClientCaller with the specified gRPC channel.
	 *
	 * @param chl The ManagedChannel used for establishing the gRPC communication channel
	 * @throws IllegalArgumentException if the provided channel is null
	 */
	public BidirectionalStreamingGrpcClientCaller(ManagedChannel chl) {
		channel = chl;
		asyncStub = HelloGrpc.newStub(channel);
	}

	/**
	 * Performs a bidirectional streaming RPC call to send multiple HelloRequest messages.
	 *
	 * This method creates a list of HelloRequest objects using predefined names and randomly generated ages,
	 * then sends them to the server using a bidirectional streaming RPC. It handles responses and potential
	 * errors asynchronously using StreamObservers.
	 *
	 * @throws InterruptedException if the await operation on the CountDownLatch is interrupted
	 *
	 * @implNote
	 * - Logs the start of the send operation
	 * - Generates HelloRequest objects with names from the class's names array
	 * - Uses a CountDownLatch to synchronize the asynchronous streaming operation
	 * - Handles server responses by logging each response
	 * - Manages potential errors by logging warning and canceling the RPC if needed
	 * - Waits up to one minute for the streaming operation to complete
	 *
	 * @see StreamObserver
	 * @see CountDownLatch
	 */
	@Override
	public void send() throws InterruptedException {
		log.info(">>> Send Call");

		List<HelloRequest> helloRequestList = new ArrayList<>();
		for (String name: names) {
			helloRequestList.add(
				HelloRequest.newBuilder()
					.setName(name)
					.setAge(getRandomAge())
					.setMessage("Proto Object")
					.build()
			);
		}

		final CountDownLatch finishLatch = new CountDownLatch(1);
		StreamObserver<HelloResponse> responseObserver = new StreamObserver<>() {
			@Override
			public void onNext(HelloResponse helloResponse) {
				log.info(">>> Response Data => [%s]".formatted(helloResponse));
			}

			@Override
			public void onError(Throwable t) {  // 스트림에서 종료 오류 발생 시 수신
				Status status = Status.fromThrowable(t);
				log.warn(">>> Warning => [%s]".formatted(status));
				finishLatch.countDown();
			}

			@Override
			public void onCompleted() {  // 스트림이 성공적으로 완료되었다고 응답 받음
				log.info(">>> Finished.");
				finishLatch.countDown();
			}
		};

		StreamObserver<HelloRequest> requestObserver = asyncStub.bidiHello(responseObserver);
		try {
			for (HelloRequest req: helloRequestList) {
				requestObserver.onNext(req);
			}
		} catch (RuntimeException e) {
			// Cancel RPC
			requestObserver.onError(e);
			throw e;
		}
		// Mark the end of requests
		requestObserver.onCompleted();

		// Receiving happens asynchronously
		finishLatch.await(1, TimeUnit.MINUTES);
	}

	/**
	 * Generates a random integer age within the specified range.
	 *
	 * @return A random integer between {@code minAge} and {@code maxAge}, inclusive.
	 */
	private int getRandomAge() {
		return (int) (Math.random() * (maxAge - minAge + 1)) + minAge;
	}
}