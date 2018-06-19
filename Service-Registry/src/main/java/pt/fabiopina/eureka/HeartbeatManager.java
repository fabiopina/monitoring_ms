package pt.fabiopina.eureka;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.fabiopina.entities.ClientHeartbeatEntity;
import pt.fabiopina.entities.EventInfoEntity;
import pt.fabiopina.queue.EventQueue;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeartbeatManager {
    private Logger logger = LoggerFactory.getLogger(HeartbeatManager.class);
    private Map<String, ClientHeartbeatEntity> clients;
    private EventQueue eventQueue;
    private int heartbeatInterval;
    private String eurekaURL;

    public HeartbeatManager(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
        clients = new LinkedHashMap<>();
        heartbeatInterval = Integer.parseInt(System.getenv("HEARTBEATINTERVAL"));
        eurekaURL = "http://" + System.getenv("EUREKA") + "/eureka/apps/";
    }

    public void addClient(String containerID, ClientHeartbeatEntity client) {
        clients.put(containerID, client);
    }

    public void removeClient(String containerID) {
        clients.remove(containerID);
    }

    public boolean isClient(String containerID) {
        if (clients.get(containerID) == null) return false;
        return true;
    }

    public void renew() {
        logger.info("Starting heartbeat manager ...");
        while (true) {
            try {
                Thread.sleep(heartbeatInterval * 1000);
                logger.info("Starting heartbeat round ...");
                for (String key : clients.keySet()) {
                    String url = eurekaURL + clients.get(key).getAppName() + "/" + clients.get(key).getInstanceID();
                    logger.info("PUT " + url);

                    HttpResponse<JsonNode> jsonResponse = Unirest.put(url)
                            .header("Content-Type", "application/json")
                            .asJson();
                    logger.info("Eureka response: " + jsonResponse.getStatus());
                    if (jsonResponse.getStatus() == 404) {
                        eventQueue.addEvent(new EventInfoEntity("start", clients.get(key).getContainerID(), clients.get(key).getNamespace(), clients.get(key).getServiceName(), clients.get(key).getIpAddr(), clients.get(key).getPort()));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getAppName(String containerID) {
        return clients.get(containerID).getAppName();
    }

    public String getInstanceID(String containerID) {
        return clients.get(containerID).getInstanceID();
    }
}
