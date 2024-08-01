FROM openjdk:17
LABEL authors="aww"

ARG JAR_FILE=./target/web-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} url-shortener.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "url-shortener.jar"]