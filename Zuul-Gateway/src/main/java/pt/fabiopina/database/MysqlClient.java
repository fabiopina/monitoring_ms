package pt.fabiopina.database;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.fabiopina.entities.CleanInfoEntity;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MysqlClient {
    private Logger logger = LoggerFactory.getLogger(MysqlClient.class);
    private Connection conn = null;

    public Connection getConnection() {
        if (conn != null) return conn;

        Ini ini = null;
        try {
            ini = new Ini(new File("config.ini"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.util.prefs.Preferences prefs = new IniPreferences(ini);
        String user = prefs.node("MySQL").get("user", null);
        String password = prefs.node("MySQL").get("password", null);
        String database = prefs.node("MySQL").get("database", null);
        String host = System.getenv("DATABASEADDRESS");

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
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + System.getenv("TABLE") + " (" +
                        "id INT NOT NULL AUTO_INCREMENT," +
                        "start_time BIGINT," +
                        "end_time BIGINT," +
                        "duration BIGINT," +
                        "source_ip VARCHAR(255)," +
                        "source_port INT," +
                        "destiny_microservice VARCHAR(255)," +
                        "destiny_instance VARCHAR(255)," +
                        "destiny_ip VARCHAR(255)," +
                        "destiny_function VARCHAR(255)," +
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

                Statement stmt = getConnection().createStatement();
                stmt.executeUpdate("INSERT INTO " + System.getenv("TABLE") + " (start_time, end_time, duration, source_ip, source_port, destiny_microservice, destiny_instance, destiny_ip, destiny_function)" +
                        " VALUES ("+ info.getStartTime() +
                        ", " + info.getEndTime() + "" +
                        ", " + info.getDuration() + "" +
                        ", '" + info.getSourceIpAddr() + "'" +
                        ", " + info.getSourcePort() + "" +
                        ", '" + info.getDestinyMicroservice() + "'" +
                        ", '" + info.getDestinyInstance() + "'" +
                        ", '" + info.getDestinyIpAddr() + "'" +
                        ", '" + info.getDestinyFunction() + "')");
                break;
            } catch (Exception e) {
                failed = true;
                conn = null;
                logger.info("MySQL Database is down. Reconnecting in 5 seconds ...");
            }
        }
    }
}
