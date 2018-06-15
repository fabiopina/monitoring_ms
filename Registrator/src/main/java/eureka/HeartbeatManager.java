package eureka;

import entity.InfoClient;
import http.HttpClient;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class HeartbeatManager {
    private Map<String, InfoClient> clients;
    private int heartbeatInterval;
    private String eurekaURL, servicePath;

    public HeartbeatManager() {
        clients = new LinkedHashMap<String, InfoClient>();
        heartbeatInterval = Integer.parseInt(System.getenv("HEARTBEATINTERVAL"));
        eurekaURL = "http://" + System.getenv("EUREKA") + "/eureka/";
        servicePath = System.getenv("SERVICEPATH");
    }

    public void addClient(String containerID, InfoClient info) {
        clients.put(containerID, info);
    }

    public void removeClient(String containerID) {
        clients.remove(containerID);
    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                while (true) {
                    try{
                        Thread.sleep(heartbeatInterval * 1000);
                        renew();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }});
        thread.start();
    }

    public void renew() {
        System.out.println("Starting heartbeat round: " + new Timestamp(System.currentTimeMillis()).toString());
        for (String key : clients.keySet()) {
            String url = eurekaURL + servicePath + "/" + clients.get(key).getAppID() + "/" + clients.get(key).getInstanceID();
            System.out.println("PUT " + url);
            try {
                int code = HttpClient.put(url);
                System.out.println("RESPONSE CODE: " + code);
                if (code == 404) {
                    String image = clients.get(key).getAppID();
                    String port = clients.get(key).getPort();
                    String hostname = clients.get(key).getHostname();
                    String ipAddr = clients.get(key).getIpAddr();
                    removeClient(key);

                    new Eureka().register(this, key, image, port, hostname, ipAddr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getAppID(String containerID) {
        return clients.get(containerID).getAppID();
    }

    public String getInstanceID(String containerID) {
        return clients.get(containerID).getInstanceID();
    }
}


