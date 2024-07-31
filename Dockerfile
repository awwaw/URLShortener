FROM openjdk:22
LABEL authors="aww"

ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "url-shortener.jar"]