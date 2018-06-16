package eureka;

import entity.InfoClient;
import http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeartbeatManager {
    private Logger logger = LoggerFactory.getLogger(HeartbeatManager.class);
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
        logger.info("Starting heartbeat round ...");
        for (String key : clients.keySet()) {
            String url = eurekaURL + servicePath + "/" + clients.get(key).getAppID() + "/" + clients.get(key).getInstanceID();
            logger.info("PUT " + url);
            try {
                int code = HttpClient.put(url);
                logger.info("RESPONSE CODE: " + code);
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


