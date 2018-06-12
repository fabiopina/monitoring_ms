# Use an official Java runtime as a parent image
FROM openjdk:8-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
ADD . /app/

# Make port 4000 available to the world outside this container
EXPOSE 4000

ADD /target/ZuulGateway.jar ZuulGateway.jar

ENTRYPOINT ["java","-jar","ZuulGateway.jar"]
