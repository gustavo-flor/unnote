FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /build

COPY pom.xml .
COPY src src

RUN mvn clean install -DskipTests

FROM openjdk:17-slim AS app

WORKDIR /app

COPY --from=build /build/target/*.jar /app.jar
COPY docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
