package io.solo.greet;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.solo.greet.GreeterGrpc.GreeterFutureStub;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class GreetWebfluxGrpcClientApplication {

	private static final Logger logger = Logger.getLogger(GreetWebfluxGrpcClientApplication.class.getName());

	private GreeterFutureStub futureStub;

	@PostConstruct
	public void init() throws InterruptedException {
		String target = "hello-grpc-server:8080";
		ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
		futureStub = GreeterGrpc.newFutureStub(channel);
	}

	public static void main(String[] args) {
		SpringApplication.run(GreetWebfluxGrpcClientApplication.class, args);
	}

	@RestController
	public class GreetingController {

		@GetMapping("/greeting/{name}")
		public String greeting(@PathVariable String name) throws InterruptedException, ExecutionException {
			logger.info("Will try to greet " + name + " ...");
			HelloRequest request = HelloRequest.newBuilder().setName(name).build();
			ListenableFuture<HelloReply> responseFuture;
			try {
				responseFuture = futureStub.sayHello(request);
			} catch (StatusRuntimeException e) {
				logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
				throw e;
			}

			// TODO: Do this in a reactive way, but need to find a good way for Google's ListenableFuture to Mono
			return responseFuture.get().getMessage();
		}
	}
}
