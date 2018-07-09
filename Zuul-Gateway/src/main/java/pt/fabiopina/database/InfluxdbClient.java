package pt.fabiopina.database;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.fabiopina.entities.CleanInfoEntity;

import java.util.concurrent.TimeUnit;

public class InfluxdbClient {
    private Logger logger = LoggerFactory.getLogger(MysqlClient.class);
    private InfluxDB influxDB = null;
    private String dbName;

    public InfluxdbClient() {
        dbName = System.getenv("INFLUXDB_DB");
    }

    public InfluxDB getConnection() {
        if (influxDB != null) return influxDB;

        String user = System.getenv("INFLUXDB_USER");
        String password = System.getenv("INFLUXDB_USER_PASSWORD");
        String host = System.getenv("INFLUXDB_ADDRESS");

        return getConnection("http://" + host + ":8086", user, password);
    }

    public InfluxDB getConnection(String host, String user, String pass){
        boolean failed = false;
        while (true) {
            try {
                if(failed) Thread.sleep(5000);
                influxDB = InfluxDBFactory.connect(host, user, pass);
                logger.info("Connected to InfluxData with success!");
                break;

            } catch (Exception e) {
                failed = true;
                logger.info("InfluxData is down. Reconnecting in 5 seconds ...");
            }
        }
        return influxDB;
    }

    public void addEntry(CleanInfoEntity info) {
        boolean failed = false;
        while (true) {
            try {
                if(failed) Thread.sleep(5000);

                BatchPoints batchPoints = BatchPoints
                        .database(dbName)
                        .tag("async", "true")
                        .consistency(InfluxDB.ConsistencyLevel.ALL)
                        .build();

                Point point = Point.measurement("request")
                        .time(info.getStartTimestamp(), TimeUnit.MILLISECONDS)
                        .addField("duration", info.getDuration())
                        .addField("source_ip", info.getSourceIpAddr())
                        .addField("source_port", info.getSourcePort())
                        .addField("destiny_microservice", info.getDestinyMicroservice())
                        .addField("destiny_instance", info.getDestinyInstance())
                        .addField("destiny_port", info.getDestinyPort())
                        .addField("destiny_ip", info.getDestinyIpAddr())
                        .addField("method", info.getMethod())
                        .addField("url", info.getUrl())
                        .addField("status_code", info.getStatusCode())
                        .build();

                batchPoints.point(point);
                getConnection().write(batchPoints);

                break;
            } catch (Exception e) {
                failed = true;
                influxDB = null;
                logger.info("InfluxData is down. Reconnecting in 5 seconds ...");
            }
        }
    }
}
