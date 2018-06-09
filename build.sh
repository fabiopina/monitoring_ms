#!/bin/bash

# Delete containers
docker rm microservices_aggr_1
docker rm microservices_playlists_1
docker rm microservices_auth_1
docker rm microservices_users_1
docker rm microservices_songs_1
docker rm microservices_zuul_1
docker rm microservices_eureka_1
docker rm microservices_log-server_1

# Delete images
docker rmi users-ms
docker rmi fabiopina151/microservices:users-ms
docker rmi songs-ms
docker rmi fabiopina151/microservices:songs-ms
docker rmi playlists-ms
docker rmi fabiopina151/microservices:playlists-ms
docker rmi aggr-ms
docker rmi fabiopina151/microservices:aggr-ms
docker rmi auth-ms
docker rmi fabiopina151/microservices:auth-ms
docker rmi eureka-server
docker rmi fabiopina151/microservices:eureka-server
docker rmi zuul-gateway
docker rmi fabiopina151/microservices:zuul-gateway
docker rmi log-server
docker rmi fabiopina151/microservices:log-server

# Zuul
cd Zuul-Gateway
mvn clean
mvn install
docker build -t zuul-gateway .
cd ..

# Eureka
cd Eureka-Server
mvn clean
mvn install
docker build -t eureka-server .
cd ..

# Aggregator
cd Aggregator_MS
docker build -t aggr-ms .
cd ..

# Authentication
cd Authentication_MS
docker build -t auth-ms .
cd ..

# Playlists
cd Playlists_MS
docker build -t playlists-ms .
cd ..

# Songs
cd Songs_MS
docker build -t songs-ms .
cd ..

# Users
cd Users_MS
docker build -t users-ms .
cd ..

# Log-Server
cd Log_Server
docker build -t log-server .
cd ..

# Upload images to docker hub
docker tag zuul-gateway:latest fabiopina151/microservices:zuul-gateway
docker push fabiopina151/microservices:zuul-gateway

docker tag eureka-server:latest fabiopina151/microservices:eureka-server
docker push fabiopina151/microservices:eureka-server

docker tag aggr-ms:latest fabiopina151/microservices:aggr-ms
docker push fabiopina151/microservices:aggr-ms

docker tag auth-ms:latest fabiopina151/microservices:auth-ms
docker push fabiopina151/microservices:auth-ms

docker tag playlists-ms:latest fabiopina151/microservices:playlists-ms
docker push fabiopina151/microservices:playlists-ms

docker tag songs-ms:latest fabiopina151/microservices:songs-ms
docker push fabiopina151/microservices:songs-ms

docker tag users-ms:latest fabiopina151/microservices:users-ms
docker push fabiopina151/microservices:users-ms

docker tag log-server:latest fabiopina151/microservices:log-server
docker push fabiopina151/microservices:log-server




