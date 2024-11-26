# build
FROM gradle:8.8.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# run
FROM eclipse-temurin:21-jdk AS prod
WORKDIR /app
COPY --from=build /app/build/libs/sonisori-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
