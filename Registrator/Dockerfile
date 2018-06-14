# Use an official Java runtime as a parent image
FROM openjdk:8-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
ADD . /app/

ADD /target/Registrator.jar Registrator.jar

ENTRYPOINT ["java","-jar","Registrator.jar"]
