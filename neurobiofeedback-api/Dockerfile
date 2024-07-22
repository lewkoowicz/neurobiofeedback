FROM openjdk:17-jdk-slim

MAINTAINER lewkoowicz

COPY target/neurobiofeedback-api-0.0.1-SNAPSHOT.jar neurobiofeedback-api-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "neurobiofeedback-api-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]