# Hello Service with gRPC Server

```
./mvnw spring-boot:run
```

```
grpcurl -plaintext localhost:50051 hello.Greeter/SayHello
```


# Greet Service with WebFlux Server and gRPC Client

```
./mvnw spring-boot:run
```

```
curl localhost:8080
```