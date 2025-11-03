# ----------- Stage 1: Build with Maven -----------
# FROM maven:eclipse-temurin-25-alpine AS build

# WORKDIR /build
# COPY . .
# RUN mvn clean package -DskipTests

# ----------- Stage 2: Run the JAR -----------
FROM azul/zulu-openjdk-alpine:25

# Create a non-root user and group
RUN addgroup -S numina && adduser -S numina -G numina

# Create app directory
WORKDIR /app

# Copy built jar from previous stage
COPY --from=build /build/target/numina-backend-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown numina:numina /app/app.jar

# Switch to non-root user
USER numina

# Expose port (adjust if needed)
EXPOSE 8080

# Sleep for 30 seconds before starting the application
CMD ["sh", "-c", "sleep 30 && exec java -jar /app/app.jar"]
