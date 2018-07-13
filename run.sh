#!/bin/bash

python script.py
sudo docker rmi grafana
cd Grafana
sudo docker build -t grafana . 
cd ..
sudo docker stack deploy -c infrastructure.yml infra
