FROM gradle:jdk21 AS build

WORKDIR /home/gradle/src

COPY . .

RUN gradle :nyo-chat:bootJar --no-daemon

FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /home/gradle/src/nyo-chat/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]