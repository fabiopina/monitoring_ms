[Imgur](https://i.imgur.com/knWC5eD.png)
How to use
======
### Prerequisites
- Python 3
- Docker-ce
- Configure docker swarm nodes
- Configure a docker network of type overlay

### Running It
Clone the repository into a leader node, then change configurations from config.yml: 
```yaml
network: my-network
eureka:
	expose_port: 8761
zuul:
	expose_port: 8765
grafana:
	expose_port: 3000
maria_db:
	expose_port: 3306
	root_password: root
	user: user
	user_password: password
	database: db0
influx_db:
	expose_port: 8086
	admin_user: admin
	admin_password: admin
	user: user
	user_password: password
	database: db0
```
Then run: 
```
./run.sh
```




