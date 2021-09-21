# Hello Service with gRPC Server

```
./mvnw spring-boot:run
```

```
grpcurl -plaintext localhost:50051 hello.Greeter/SayHello
```

```
docker build -t murphye/hello-grpc-server .
docker run -p 8080:8080 murphye/hello-grpc-server
docker push murphye/hello-grpc-server
```


# Greet Service with WebFlux Server and gRPC Client

```
./mvnw spring-boot:run
```

```
curl localhost:8080
```

```
docker build -t murphye/greet-webflux-grpc-client .
docker run -p 8080:8080 murphye/greet-webflux-grpc-client
docker push murphye/greet-webflux-grpc-client
```