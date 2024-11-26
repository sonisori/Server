# build
FROM gradle:8.8.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# run
FROM eclipse-temurin:21-jdk AS prod
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY src/main/resources/application.properties ./src/main/resources/application.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
