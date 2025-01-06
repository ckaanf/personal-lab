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

	public ClientStreamingGrpcClientCaller(ManagedChannel chl) {
		channel = chl;
		asyncStub = HelloGrpc.newStub(channel);
	}

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

	private int getRandomAge() {
		return (int) (Math.random() * (maxAge - minAge + 1)) + minAge;
	}
}