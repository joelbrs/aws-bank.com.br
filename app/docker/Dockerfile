# Use OpenJDK 21 as the base image
FROM eclipse-temurin:25

# Set the working directory inside the container
WORKDIR /app

ARG VERSION=0.1.0

# Copy the JAR file into the container
COPY target/app-${VERSION}.jar app.jar

# Expose port 9090 to access the application
EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]