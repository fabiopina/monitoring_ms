package pt.fabiopina.eureka;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.fabiopina.entities.ClientHeartbeatEntity;
import pt.fabiopina.entities.EventInfoEntity;

public class EurekaClient {
    private Logger logger = LoggerFactory.getLogger(EurekaClient.class);
    private HeartbeatManager heartbeatManager;
    private String eurekaURL, containerID, image, hostName, ipAddr, port;

    public EurekaClient(HeartbeatManager heartbeatManager, String containerID) {
        this.heartbeatManager = heartbeatManager;
        this.containerID = containerID;
        this.eurekaURL = "http://" + System.getenv("EUREKA") + "/eureka/apps/";
    }

    public EurekaClient(HeartbeatManager heartbeatManager, EventInfoEntity currentClient) {
        this.heartbeatManager = heartbeatManager;
        this.containerID = currentClient.getContainerID();
        this.image = currentClient.getImage();
        this.hostName = currentClient.getContainerID().substring(0,12);
        this.ipAddr = currentClient.getIpAddr();
        this.port = currentClient.getPort();
        this.eurekaURL = "http://" + System.getenv("EUREKA") + "/eureka/apps/";
    }

    public String getAppName() {
        if (image.contains("/")) image = image.substring(image.indexOf("/")+1).trim();
        if (image.contains(":")) image = image.split(":")[0];
        return image.replace("registerwitheureka_", "");
    }

    public String getInstanceID() {
        return hostName + ":" + getAppName() + ":" + port;
    }

    public String getInstanceData() {
        return "{\"instance\": {" +
                "\"app\": \"" + getAppName() + "\", " +
                "\"instanceId\": \"" + getInstanceID() + "\", " +
                "\"hostName\": \"" + hostName + "\", " +
                "\"ipAddr\": \"" + ipAddr + "\", " +
                "\"healthCheckUrl\": \"http://" + hostName + ":" + port + "/health\", " +
                "\"statusPageUrl\": \"http://" + hostName + ":" + port + "/info\", " +
                "\"homePageUrl\": \"http://" + hostName + ":" + port + "/\", " +
                "\"port\": {" +
                "\"$\": " + port + ", " +
                "\"@enabled\": true}, " +
                "\"vipAddress\": \"" + getAppName() + "\", " +
                "\"secureVipAddress\": \"" + getAppName() + "\", " +
                "\"dataCenterInfo\": {" +
                "\"@class\": \"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\", " +
                "\"name\": \"MyOwn\"}, " +
                "\"status\": \"UP\"}}";
    }

    public void startRegistrationProcess() {
        if (!heartbeatManager.isClient(containerID)) {
            register();
            heartbeatManager.addClient(containerID, new ClientHeartbeatEntity(getAppName(), getInstanceID(), containerID, image, ipAddr, port));
        }
    }

    public void register() {
        logger.info("Registering service: " + getAppName());
        String url = eurekaURL + getAppName();
        logger.info("POST " + url);

        boolean failed = false;
        while (true) {
            try {
                if(failed) Thread.sleep(5000);
                HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                        .header("Content-Type", "application/json")
                        .body(getInstanceData())
                        .asJson();
                logger.info("Eureka response: " + jsonResponse.getStatus());
                break;

            } catch (Exception e) {
                failed = true;
                logger.info("Error in Eureka request. Retrying in 5 seconds ...");
            }
        }
    }

    public void deregister() {
        logger.info("De-Registering service: " + heartbeatManager.getAppName(containerID));
        String url = eurekaURL + heartbeatManager.getAppName(containerID) + "/" + heartbeatManager.getInstanceID(containerID);
        logger.info("DELETE " + url);

        boolean failed = false;
        while (true) {
            try {
                if(failed) Thread.sleep(5000);
                HttpResponse<JsonNode> jsonResponse = Unirest.delete(url)
                        .header("Content-Type", "application/json")
                        .asJson();
                logger.info("Eureka response: " + jsonResponse.getStatus());
                if (jsonResponse.getStatus() == 200) heartbeatManager.removeClient(containerID);
                break;

            } catch (Exception e) {
                failed = true;
                logger.info("Error in Eureka request. Retrying in 5 seconds ...");
            }
        }
    }
}
