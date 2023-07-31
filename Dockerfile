# Use an OpenJDK 17 base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot executable JAR file into the container
COPY target/weather-app-0.0.1-SNAPSHOT.jar /app/weather-app-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Set the command to run your Spring Boot application
CMD ["java", "-jar", "weather-app-0.0.1-SNAPSHOT.jar"]
