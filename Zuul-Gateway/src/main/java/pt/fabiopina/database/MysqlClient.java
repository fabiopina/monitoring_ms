package pt.fabiopina.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.fabiopina.entities.CleanInfoEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class MysqlClient {
    private Logger logger = LoggerFactory.getLogger(MysqlClient.class);
    private Connection conn = null;

    public Connection getConnection() {
        if (conn != null) return conn;

        String user = System.getenv("DBUSER");
        String password = System.getenv("DBPASSWORD");
        String database = System.getenv("DBDATABASE");
        String host = System.getenv("DBADDRESS");

        return getConnection(host, database, user, password);
    }

    public Connection getConnection(String host, String db, String user, String pass){
        boolean failed = false;
        while (true) {
            try {
                if(failed) Thread.sleep(5000);
                conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + db + "?user=" + user + "&password=" + pass + "&serverTimezone=UTC&autoReconnect=true&useSSL=false");
                logger.info("Connected to MySQL Database with success!");
                break;

            } catch (Exception e) {
                failed = true;
                logger.info("MySQL Database is down. Reconnecting in 5 seconds ...");
            }
        }
        return conn;
    }

    public void createTable() {
        boolean failed = false;
        while (true) {
            try {
                if(failed) Thread.sleep(5000);

                Statement stmt = getConnection().createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + System.getenv("DBTABLE") + " (" +
                        "id INT NOT NULL AUTO_INCREMENT," +
                        "start_date DATETIME(3)," +
                        "end_date DATETIME(3)," +
                        "start_timestamp BIGINT," +
                        "end_timestamp BIGINT," +
                        "duration BIGINT," +
                        "source_ip VARCHAR(255)," +
                        "source_port VARCHAR(255)," +
                        "destiny_microservice VARCHAR(255)," +
                        "destiny_instance VARCHAR(255)," +
                        "destiny_port VARCHAR(255)," +
                        "destiny_ip VARCHAR(255)," +
                        "method VARCHAR(255)," +
                        "url VARCHAR(255)," +
                        "status_code VARCHAR(255)," +
                        "PRIMARY KEY(id))");
                break;
            } catch (Exception e) {
                failed = true;
                conn = null;
                logger.info("MySQL Database is down. Reconnecting in 5 seconds ...");
            }
        }

    }

    public void addEntry(CleanInfoEntity info) {
        boolean failed = false;
        while (true) {
            try {
                if(failed) Thread.sleep(5000);

                PreparedStatement ps = getConnection().prepareStatement("INSERT INTO " + System.getenv("DBTABLE") + " (start_date, end_date, start_timestamp, end_timestamp, duration, source_ip, source_port, destiny_microservice, destiny_instance, destiny_port, destiny_ip, method, url, status_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                ps.setTimestamp(1, info.getStartDate());
                ps.setTimestamp(2, info.getEndDate());
                ps.setLong(3, info.getStartTimestamp());
                ps.setLong(4, info.getEndTimestamp());
                ps.setLong(5, info.getDuration());
                ps.setString(6, info.getSourceIpAddr());
                ps.setString(7, info.getSourcePort());
                ps.setString(8, info.getDestinyMicroservice());
                ps.setString(9, info.getDestinyInstance());
                ps.setString(10, info.getDestinyPort());
                ps.setString(11, info.getDestinyIpAddr());
                ps.setString(12, info.getMethod());
                ps.setString(13, info.getUrl());
                ps.setString(14, info.getStatusCode());
                ps.executeUpdate();

                break;
            } catch (Exception e) {
                e.printStackTrace();
                failed = true;
                conn = null;
                logger.info("MySQL Database is down. Reconnecting in 5 seconds ...");
            }
        }
    }
}
