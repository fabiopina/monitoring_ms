package hello.database;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    public DatabaseConnection() { }

    public Connection connect() {
        Connection conn = null;
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

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?serverTimezone=UTC&" + "user=" + user + "&password=" + password);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return conn;
    }

    public void createTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + System.getenv("TABLE") + " (" +
                    "id INT NOT NULL AUTO_INCREMENT," +
                    "start_time BIGINT," +
                    "end_time BIGINT," +
                    "request_time_milliseconds BIGINT," +
                    "source_ip VARCHAR(255)," +
                    "source_port INT," +
                    "destiny_microservice VARCHAR(255)," +
                    "destiny_instance VARCHAR(255)," +
                    "destiny_ip VARCHAR(255)," +
                    "destiny_function VARCHAR(255)," +
                    "PRIMARY KEY(id))");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEntry(Connection conn, long start, long end, long time, String s_ip, int s_port, String d_micro, String d_insta, String d_ip, String d_func) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO " + System.getenv("TABLE") + " (start_time, end_time, request_time_milliseconds, source_ip, source_port, destiny_microservice, destiny_instance, destiny_ip, destiny_function)" +
                    " VALUES ("+ start +
                    ", " + end + "" +
                    ", " + time + "" +
                    ", '" + s_ip + "'" +
                    ", " + s_port + "" +
                    ", '" + d_micro + "'" +
                    ", '" + d_insta + "'" +
                    ", '" + d_ip + "'" +
                    ", '" + d_func + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}