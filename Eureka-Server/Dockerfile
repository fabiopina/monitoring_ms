# Use an official Java runtime as a parent image
FROM openjdk:8-jdk-alpine

# Make port 8761 available to the world outside this container
EXPOSE 8761

ADD /target/EurekaServer.jar EurekaServer.jar

ENTRYPOINT ["java","-jar","EurekaServer.jar"]
