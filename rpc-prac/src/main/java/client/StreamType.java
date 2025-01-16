package client;

import client.bidirectional_stream_rpc.BidirectionalStreamingGrpcClientCaller;
import client.client_stream_rpc.ClientStreamingGrpcClientCaller;
import client.server_stream_rpc.ServerStreamingGrpcClientCaller;
import client.simple_rpc.SimpleGrpcClientCaller;
import io.grpc.ManagedChannel;
import lombok.Getter;

@Getter
public enum StreamType {
	SIMPLE("simple") {
		/**
		 * Creates a simple gRPC client caller for unary RPC communication.
		 *
		 * @param channel The managed gRPC channel used for establishing the connection
		 * @return A {@code SimpleGrpcClientCaller} instance configured with the provided channel
		 */
		@Override
		public GrpcClientCaller createCaller(ManagedChannel channel) {
			return new SimpleGrpcClientCaller(channel);
		}
	},
	SERVER_STREAM("server_stream") {
		/**
		 * Creates a gRPC client caller for server-streaming RPC communication.
		 *
		 * @param channel The managed gRPC channel used for establishing the network connection
		 * @return A {@code ServerStreamingGrpcClientCaller} configured with the specified channel
		 */
		@Override
		public GrpcClientCaller createCaller(ManagedChannel channel) {
			return new ServerStreamingGrpcClientCaller(channel);
		}
	},
	CLIENT_STREAM("client_stream") {
		/**
		 * Creates a client-streaming gRPC client caller for the specified managed channel.
		 *
		 * @param channel The managed gRPC channel to be used for creating the client caller
		 * @return A {@link ClientStreamingGrpcClientCaller} configured with the provided channel
		 */
		@Override
		public GrpcClientCaller createCaller(ManagedChannel channel) {
			return new ClientStreamingGrpcClientCaller(channel);
		}
	},
	BI_DIRECTIONAL("bi_directional") {
		/**
		 * Creates a bidirectional streaming gRPC client caller for the specified managed channel.
		 *
		 * @param channel The managed gRPC channel used to establish the bidirectional stream
		 * @return A {@code BidirectionalStreamingGrpcClientCaller} configured with the provided channel
		 */
		@Override
		public GrpcClientCaller createCaller(ManagedChannel channel) {
			return new BidirectionalStreamingGrpcClientCaller(channel);
		}
	};

	private final String desc;

	/**
	 * Constructs a StreamType enum constant with a descriptive string.
	 *
	 * @param desc A human-readable description of the stream type
	 */
	StreamType(String desc) {
		this.desc = desc;
	}

	/**
 * Creates a gRPC client caller specific to the current stream type.
 *
 * @param channel the gRPC managed channel to be used for creating the client caller
 * @return a specialized GrpcClientCaller implementation corresponding to the stream type
 */
public abstract GrpcClientCaller createCaller(ManagedChannel channel);
}
