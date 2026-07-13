# ---- Build stage ----
FROM gradle:7.6.4-jdk17 AS build
WORKDIR /app
COPY settings.gradle build.gradle ./
COPY src ./src
RUN gradle bootJar --no-daemon

# ---- Run stage ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# Render injects $PORT; Spring reads it via server.port=${PORT:8080}.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
