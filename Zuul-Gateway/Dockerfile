# Use an official Java runtime as a parent image
FROM openjdk:8-jdk-alpine

# Make port 4000 available to the world outside this container
EXPOSE 4000

ADD /target/ZuulGateway.jar ZuulGateway.jar

ENTRYPOINT ["java","-jar","ZuulGateway.jar"]
