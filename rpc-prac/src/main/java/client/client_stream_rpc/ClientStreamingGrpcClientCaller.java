package client.client_stream_rpc;

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
public class ClientStreamingGrpcClientCaller implements GrpcClientCaller {

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
	 * Constructs a new ClientStreamingGrpcClientCaller with the specified gRPC channel.
	 *
	 * @param chl the ManagedChannel to be used for gRPC communication
	 * @throws IllegalArgumentException if the provided channel is null
	 */
	public ClientStreamingGrpcClientCaller(ManagedChannel chl) {
		channel = chl;
		asyncStub = HelloGrpc.newStub(channel);
	}

	/**
	 * Sends a stream of client-side gRPC requests using client streaming.
	 *
	 * This method creates a list of {@code HelloRequest} objects with names and random ages,
	 * then sends them asynchronously to the server using a client streaming RPC.
	 * It manages the request stream, handles responses, and ensures proper completion.
	 *
	 * @throws InterruptedException if the request stream is interrupted during processing
	 *
	 * @implNote
	 * - Generates requests for each name in the predefined names array
	 * - Uses a {@code CountDownLatch} to manage asynchronous request completion
	 * - Logs each request and response
	 * - Stops sending requests if an error occurs during streaming
	 * - Waits up to 1 minute for the stream to complete
	 *
	 * @see StreamObserver
	 * @see HelloRequest
	 * @see HelloResponse
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

		StreamObserver<HelloRequest> requestObserver = asyncStub.lotsOfGreetings(responseObserver);
		try {
			for (HelloRequest req: helloRequestList) {
				requestObserver.onNext(req);
				log.info(">>> Req Name: " + req.getName());

				Thread.sleep(1000);
				if (finishLatch.getCount() == 0) {  // 오류 발생 시 다음 코드를 전송하더라도 처리되지 않기 때문에 전송하지 않도록 처리
					// RPC completed or errored before we finished sending.
					// Sending further requests won't error, but they will just be thrown away.
					log.info(">>> Stop the next request");
					return;
				}
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

		log.info(">>> End.");
	}

	/**
	 * Generates a random age within the predefined age range.
	 *
	 * @return A random integer representing an age between {@code minAge} and {@code maxAge}, inclusive.
	 */
	private int getRandomAge() {
		return (int) (Math.random() * (maxAge - minAge + 1)) + minAge;
	}
}