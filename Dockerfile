FROM gradle:9-jdk21 as build
WORKDIR /app
COPY . /app
RUN gradle bootJar

RUN mv build/libs/dat250-1-0.0.1-SNAPSHOT.jar app.jar
FROM eclipse-temurin:21-jre
COPY --from=build app/app.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]



