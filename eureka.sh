#!/bin/bash

docker rmi fabiopina151/eureka-server:0.1
cd Eureka-Server
mvn clean
mvn install
docker build -t fabiopina151/eureka-server:0.1 .
docker push fabiopina151/eureka-server:0.1

