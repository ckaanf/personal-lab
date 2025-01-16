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
		@Override
		public GrpcClientCaller createCaller(ManagedChannel channel) {
			return new SimpleGrpcClientCaller(channel);
		}
	},
	SERVER_STREAM("server_stream") {
		@Override
		public GrpcClientCaller createCaller(ManagedChannel channel) {
			return new ServerStreamingGrpcClientCaller(channel);
		}
	},
	CLIENT_STREAM("client_stream") {
		@Override
		public GrpcClientCaller createCaller(ManagedChannel channel) {
			return new ClientStreamingGrpcClientCaller(channel);
		}
	},
	BI_DIRECTIONAL("bi_directional") {
		@Override
		public GrpcClientCaller createCaller(ManagedChannel channel) {
			return new BidirectionalStreamingGrpcClientCaller(channel);
		}
	};

	private final String desc;

	StreamType(String desc) {
		this.desc = desc;
	}

	public abstract GrpcClientCaller createCaller(ManagedChannel channel);
}
