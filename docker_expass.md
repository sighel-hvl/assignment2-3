#### Assignment 7.
I created a dockerfile containing the following:
```dockerfile
FROM gradle:9-jdk21 as build
WORKDIR /app
COPY . /app
RUN gradle bootJar

RUN mv build/libs/dat250-1-0.0.1-SNAPSHOT.jar app.jar
FROM eclipse-temurin:21-jre
COPY --from=build app/app.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
To run the image i ran the commands:
```docker build -t app```
And then
```docker run -rm -it -p 8080:8080 app bash```

To validate that the container is working an the REST endpoints are accessible, i ran the intellijhttp tests.
All the tests passes, and i am able to do all the actions that i originally was with the current implementation of the backend. 