# Build stage
FROM openjdk:21-jdk-slim AS build
WORKDIR /app

# Copy only build files first (for caching)
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build (this layer changes most often)
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM openjdk:21-jdk-slim
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]