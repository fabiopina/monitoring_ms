#!/bin/bash

docker rmi fabiopina151/service-registry:0.1
cd Service-Registry
mvn clean
mvn install
docker build -t fabiopina151/service-registry:0.1 . 
docker push fabiopina151/service-registry:0.1
