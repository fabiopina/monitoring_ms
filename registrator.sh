#!/bin/bash

docker rmi fabiopina151/registrator:0.1

cd Registrator
mvn clean
mvn install
docker build -t fabiopina151/registrator:0.1 . 
cd ..

docker push fabiopina151/registrator:0.1
