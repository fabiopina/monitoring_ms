#!/bin/bash

# Delete images
docker rmi fabiopina151/eureka-server:0.1
docker rmi fabiopina151/zuul-gateway:0.1

# Zuul
cd Zuul-Gateway
mvn clean
mvn install
docker build -t fabiopina151/zuul-gateway:0.1 .
cd ..

# Eureka
cd Eureka-Server
mvn clean
mvn install
docker build -t fabiopina151/eureka-server:0.1 .
cd ..

# Upload images to docker hub
docker push fabiopina151/zuul-gateway:0.1
docker push fabiopina151/eureka-server:0.1
