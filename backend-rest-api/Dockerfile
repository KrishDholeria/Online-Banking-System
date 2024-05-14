FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:resolve
COPY src src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/*.jar bank4ever.jar
ENTRYPOINT ["java","-jar","bank4ever.jar"]