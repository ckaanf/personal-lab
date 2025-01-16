package client;

public interface GrpcClientCaller {

	/**
 * Sends a gRPC client request that can be interrupted during execution.
 *
 * @throws InterruptedException if the sending operation is interrupted while in progress
 */
void send() throws InterruptedException;
}
