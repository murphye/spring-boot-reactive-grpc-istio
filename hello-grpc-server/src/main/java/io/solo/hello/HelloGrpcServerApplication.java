package io.solo.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

/**
 * grpcurl -plaintext localhost:50051 hello.Greeter/SayHello
 */
@SpringBootApplication
public class HelloGrpcServerApplication {

	private static final Logger logger = Logger.getLogger(HelloGrpcServerApplication.class.getName());

	private Server server;

    @PostConstruct
    public void init() throws InterruptedException, IOException {
		this.start();
		this.blockUntilShutdown();
    }

	private void start() throws IOException {
		/* The port on which the server should run */
		int port = 50051;
		server = ServerBuilder.forPort(port)
				.addService(new GreeterImpl())
				.addService(ProtoReflectionService.newInstance())
				.build()
				.start();

		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					HelloGrpcServerApplication.this.stop();
				} catch (InterruptedException e) {
					e.printStackTrace(System.err);
				}
			}
		});
	}

	private void stop() throws InterruptedException {
		if (server != null) {
			server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
		}
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

		@Override
		public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
			HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloGrpcServerApplication.class, args);
	}
}
