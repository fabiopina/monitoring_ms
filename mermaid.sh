#!/bin/bash

docker rmi fabiopina151/mermaid-syntax-server:0.1
cd Mermaid-Syntax-Server
docker build -t fabiopina151/mermaid-syntax-server:0.1 . 
docker push fabiopina151/mermaid-syntax-server:0.1
