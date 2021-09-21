FROM openjdk:16-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY agent/opentelemetry-javaagent-all.jar opentelemetry-javaagent-all.jar
ENTRYPOINT ["java","-javaagent:opentelemetry-javaagent-all.jar","-Dotel.propagators=b3,b3multi","-jar","app.jar"]