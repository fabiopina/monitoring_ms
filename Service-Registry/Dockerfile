# Use an official Java runtime as a parent image
FROM openjdk:8-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
ADD . /app/

ADD /target/ServiceRegistry.jar ServiceRegistry.jar

ENTRYPOINT ["java","-jar","ServiceRegistry.jar"]