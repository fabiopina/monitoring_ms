current = None

info = {'network': 'my-network',
        'eureka_expose_port': 8761,
        'zuul_expose_port': 8765,
        'grafana_expose_port': 3000,
        'mariadb_expose_port': 3306,
        'mariadb_root_password': 'root',
        'mariadb_user': 'user',
        'mariadb_user_password': 'password',
        'mariadb_database': 'db0',
        'influxdb_expose_port': 8086,
        'influxdb_admin_user': 'admin',
        'influxdb_admin_password': 'admin',
        'influxdb_user': 'user',
        'influxdb_user_password': 'password',
        'influxdb_database': 'db0'}


try:
    with open('config.yml') as f:
        print("Reading config...")
        for line in f.readlines():
            line = "".join(line.split())
            if line.split(':')[0] == 'network':
                if line.split(':')[1] != '':
                    info['network'] = line.split(':')[1]
            elif line.split(':')[0] == 'eureka':
                current = 'eureka'
            elif line.split(':')[0] == 'zuul':
                current = 'zuul'
            elif line.split(':')[0] == 'grafana':
                current = 'grafana'
            elif line.split(':')[0] == 'maria_db':
                current = 'mariadb'
            elif line.split(':')[0] == 'influx_db':
                current = 'influxdb'
            else:
                if line.split(':')[1] != '':
                    info[''.join([current, '_', line.split(':')[0]])] = line.split(':')[1]


except Exception:
    print("config.yaml not found! Loading default config...")


with open('infrastructure.yml', 'w') as infra:
    infra.write('version: \"3\"\n' 
                'services:\n'
                '  eureka:\n'
                '    image: fabiopina151/eureka-server:latest\n'
                '    deploy:\n'
                '      restart_policy:\n'
                '        condition: on-failure\n'
                '    ports:\n'
                '      - \"' + info['eureka_expose_port'] + ':8761\"\n'
                '    networks:\n'
                '      - my-network\n'
                '  zuul:\n'
                '    image: fabiopina151/zuul-gateway:latest\n'
                '    deploy:\n'
                '      restart_policy:\n'
                '        condition: on-failure\n'
                '    ports:\n'
                '      - \"' + info['zuul_expose_port'] + ':8765\"\n'
                '    environment:\n'
                '      - EUREKA=eureka:8761\n'
                '      - MARIADB_ADDRESS=mariadb\n'
                '      - MARIADB_DATABASE=' + info['mariadb_database'] + '\n'
                '      - MARIADB_USER=' + info['mariadb_user'] + '\n'
                '      - MARIADB_PASSWORD=' + info['mariadb_user_password'] + '\n'
                '      - INFLUXDB_ADDRESS=influxdb\n'
                '      - INFLUXDB_DB=' + info['influxdb_database'] + '\n'
                '      - INFLUXDB_USER=' + info['influxdb_user'] + '\n'
                '      - INFLUXDB_USER_PASSWORD=' + info['influxdb_user_password'] + '\n'
                '    networks:\n'
                '      - my-network\n'
                '  registry:\n'
                '    image: fabiopina151/service-registry:latest\n'
                '    deploy: \n'
                '      mode: global\n'
                '      restart_policy:\n'
                '        condition: on-failure\n'
                '    labels:\n'
                '      - \"pt.fabiopina.mma.service.registry=true\"\n'
                '    environment:\n'
                '      - EUREKA=eureka:8761\n'
                '      - HEARTBEATINTERVAL=30\n'
                '    volumes:\n'
                '      - /var/run/docker.sock:/var/run/docker.sock\n'
                '    networks:\n'
                '      - my-network\n'
                '  mariadb:\n'
                '    image: mariadb\n'
                '    deploy:\n'
                '      restart_policy:\n'
                '        condition: on-failure\n'
                '    ports:\n'
                '      - \"' + info['mariadb_expose_port'] + ':3306\"\n'
                '    environment:\n'
                '      - MYSQL_ROOT_PASSWORD=' + info['mariadb_root_password'] + '\n'
                '      - MYSQL_USER=' + info['mariadb_user'] + '\n'
                '      - MYSQL_PASSWORD=' + info['mariadb_user_password'] + '\n'
                '      - MYSQL_DATABASE=' + info['mariadb_database'] + '\n'
                '    networks:\n'
                '      - my-network\n'
                '  influxdb:\n'
                '    image: influxdb\n'
                '    deploy:\n'
                '      restart_policy:\n'
                '        condition: on-failure\n'
                '    ports:\n'
                '      - \"' + info['influxdb_expose_port'] + ':8086\"\n'
                '    environment:\n'
                '      - INFLUXDB_DB=' + info['influxdb_database'] + '\n'
                '      - INFLUXDB_ADMIN_ENABLED=true\n'
                '      - INFLUXDB_ADMIN_USER=' + info['influxdb_admin_user'] + '\n'
                '      - INFLUXDB_ADMIN_PASSWORD=' + info['influxdb_admin_password'] + '\n'
                '      - INFLUXDB_USER=' + info['influxdb_user'] + '\n'
                '      - INFLUXDB_USER_PASSWORD=' + info['influxdb_user_password'] + '\n'
                '    networks:\n'
                '      - my-network\n'
                '  grafana:\n'
                '    image: grafana\n'
                '    deploy:\n'
                '      restart_policy:\n'
                '        condition: on-failure\n'
                '    ports:\n'
                '      - \"' + info['grafana_expose_port'] + ':3000\"\n'
                '    networks:\n'
                '      - my-network\n'
                '  chord:\n'
                '    image: jaimelive/chord_service:latest\n'
                '    deploy:\n'
                '      restart_policy:\n'
                '        condition: on-failure\n'
                '    environment:\n'
                '      - DBADDRESS=mariadb\n'
                '      - DBDATABASE=' + info['mariadb_database'] + '\n'
                '      - DBTABLE=t0\n'
                '      - DBUSER=' + info['mariadb_user'] + '\n'
                '      - DBPASSWORD=' + info['mariadb_user_password'] + '\n'
                'networks:\n'
                '  my-network:\n'
                '    external:\n'
                '      name: ' + info['network'] + '\n')

with open('Grafana/provisioning/datasources/all.yml', 'w') as infra:
    infra.write('datasources:\n'
                '- name: \'MySQL\'\n'
                '  type: \'mysql\'\n'
                '  url: \'mariadb:3306\'\n'
                '  database: \'' + info['mariadb_database'] + '\'\n'
                '  user: \'' + info['mariadb_user'] + '\'\n'
                '  password: \'' + info['mariadb_user_password'] + '\'\n'
                '- name: \'InfluxDB\'\n'
                '  type: \'influxdb\'\n'
                '  access: \'proxy\'\n'
                '  url: \'http://influxdb:8086\'\n'
                '  database: \'' + info['influxdb_database'] + '\'\n'
                '  user: \'' + info['influxdb_user'] + '\'\n'
                '  password: \'' + info['influxdb_user_password'] + '\'\n')

print("Config loaded.")