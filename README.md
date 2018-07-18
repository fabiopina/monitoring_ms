Nonintrusive Monitoring of Microservice-based Architectures
======
![alt text](https://i.imgur.com/knWC5eD.png "Logo Title Text 1")
How to use
======
### Prerequisites
- Python 3
- Docker-ce
- Configure docker swarm nodes ([Check here](https://docs.docker.com/engine/swarm/swarm-tutorial/create-swarm/))
- Configure a docker network of type overlay ([Check here](https://docs.docker.com/network/overlay/))

### Running infrastructure
Clone the repository into a leader node, then change configurations from config.yml: 
```yaml
network: my-network
eureka:
  expose: false
zuul:
  expose: false
grafana:
  expose: false
maria_db:
  expose: false
  root_password: root
  user: user
  user_password: password
  database: db0
influx_db:
  expose: false
  admin_user: admin
  admin_password: admin
  user: user
  user_password: password
  database: db0
```
In case this file is missing a default configuration is adopted and the values are the ones shown previously. By using default configuration none of the services are accessible from outside the docker network. To fix that change the `expose` tag to `true` and add the tag `expose_port` with the port you desire. 
```yaml
network: my-network
eureka:
  expose: true
  expose_port: 8761
zuul:
  expose: true
  expose_port: 8765
grafana:
  expose: true
  expose_port: 3000
maria_db:
  expose: true
  expose_port: 3306
  root_password: root
  user: user
  user_password: password
  database: db0
influx_db:
  expose: true
  expose_port: 8086
  admin_user: admin
  admin_password: admin
  user: user
  user_password: password
  database: db0
```
To start the infrastructure run the following command: 
```
$ ./run.sh
```
Check the running services by typing the following command: 
```
$ docker service ls
```
Stop the infrastructure by running:
```
$ docker stack rm infra
```
Note: This command only stops the infrastructure. If you also have costum services running they will not be stopped.
### Deploying costum services
Your services must be containerized in order to be deployed. To be recognized by the infrastructure these containers need to be in the overlay network previously created and have the label `pt.fabiopina.mma.service.registry=true`.

To deploy your services you must create a docker-compose file and run the command:
```
$ docker stack deploy -c music-example.yml music
```
`music-example.yml` should be your docker-compose file and `music` should be a namespace of your choice.
Take a look at a docker-compose file example: 
```yaml
version: "3.3"
services:
  ms-db:
    image: mariadb
    deploy: 
      replicas: 1
      restart_policy:
        condition: on-failure
    environment:
      - MYSQL_ROOT_PASSWORD=ribeiro
    networks:
      - my-network
  users:
    image: fabiopina151/users-ms:latest
    deploy: 
      replicas: 2
      restart_policy:
        condition: on-failure
    labels:
      - "pt.fabiopina.mma.registry.eureka=true"
    environment:
      - DATABASEADDRESS=ms-db:3306
    networks:
      - my-network
networks:
  my-network:
    external:
      name: my-network
```
The services will be registered in Eureka with the name: `<namespace>-<service_name>`.In order for a container to not be picked up by the infrastructure just remove the label. If you desire that a service calls another service using the Zuul gateway call it using the following address: `zuul:8765/<namespace>-<service_name>`. Take a look at the following example: 
```yaml
version: "3.3"
services:
  ms-db:
    image: mariadb
    deploy: 
      replicas: 1
      restart_policy:
        condition: on-failure
    environment:
      - MYSQL_ROOT_PASSWORD=ribeiro
    networks:
      - my-network
  users:
    image: fabiopina151/users-ms:latest
    deploy: 
      replicas: 2
      restart_policy:
        condition: on-failure
    labels:
      - "pt.fabiopina.mma.registry.eureka=true"
    environment:
      - DATABASEADDRESS=ms-db:3306
    networks:
      - my-network
  auth:
    image: fabiopina151/auth-ms:latest
    deploy: 
      replicas: 2
      restart_policy:
        condition: on-failure
    labels:
      - "pt.fabiopina.mma.registry.eureka=true"
    environment:
      - USERSADDRESS=zuul:8765/music-users
    networks:
      - my-network
networks:
  my-network:
    external:
      name: my-network
```
Note that the auth service picks up the users address using: `zuul:8765/music-users`. If you wish to stop the application run: 
```
$ docker stack rm music
```
An application example source code and docker-compose file can be found at: [https://github.com/fabiopina/music_ms ](https://github.com/fabiopina/music_ms )





