#!/bin/bash

docker rmi fabiopina151/zuul-gateway:0.1
cd Zuul-Gateway
mvn clean
mvn install
docker build -t fabiopina151/zuul-gateway:0.1 .
docker push fabiopina151/zuul-gateway:0.1

