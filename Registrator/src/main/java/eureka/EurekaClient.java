package eureka;

import entity.InfoClient;
import http.HttpClient;

public class EurekaClient {
    private HeartbeatManager heartbeat;
    private String eurekaURL, servicePath, containerID, appName, hostName, ipAddr, port;

    public EurekaClient() {
        this.eurekaURL = "http://" + System.getenv("EUREKA") + "/eureka/";
        this.servicePath = System.getenv("SERVICEPATH");
    }

    public EurekaClient(HeartbeatManager heartbeat, String containerID, String appName, String hostName, String ipAddr, String port) {
        this.heartbeat = heartbeat;
        this.containerID = containerID;
        this.appName = appName;
        this.hostName = hostName;
        this.ipAddr = ipAddr;
        this.port = port;
        this.eurekaURL = "http://" + System.getenv("EUREKA") + "/eureka/";
        this.servicePath = System.getenv("SERVICEPATH");
    }

    public String getInstanceID() {
        return hostName + ":" + appName + ":" + port;
    }

    public String getInstanceData() {
        return "{\"instance\": {" +
                "\"app\": \"" + appName + "\", " +
                "\"instanceId\": \"" + getInstanceID() + "\", " +
                "\"hostName\": \"" + hostName + "\", " +
                "\"ipAddr\": \"" + ipAddr + "\", " +
                "\"healthCheckUrl\": \"http://" + hostName + ":" + port + "/health\", " +
                "\"statusPageUrl\": \"http://" + hostName + ":" + port + "/info\", " +
                "\"homePageUrl\": \"http://" + hostName + ":" + port + "/\", " +
                "\"port\": {" +
                    "\"$\": " + port + ", " +
                    "\"@enabled\": true}, " +
                "\"vipAddress\": \"" + appName + "\", " +
                "\"secureVipAddress\": \"" + appName + "\", " +
                "\"dataCenterInfo\": {" +
                    "\"@class\": \"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\", " +
                    "\"name\": \"MyOwn\"}, " +
                "\"status\": \"UP\"}}";
    }

    public void startRegistrationProcess() {
        register();
        heartbeat.addClient(containerID, InfoClient.constructInfoClient(appName, getInstanceID(), hostName, ipAddr, port));
    }

    public void register() {
        System.out.println("Registering service: " + appName);
        String url = eurekaURL + servicePath + "/" + appName;
        System.out.println("POST " + url);

        while (true) {
            try {
                int code = HttpClient.post(url, getInstanceData());
                System.out.println("RESPONSE CODE: " + code);
                if (code == 204) {
                    break;
                }
            } catch (Exception e) {
                try{
                    System.out.println("Eureka is down. Reconnecting in 5 seconds...");
                    Thread.sleep(5000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public void deregister(HeartbeatManager heartbeat, String containerID) {
        System.out.println("De-Registering service: " + heartbeat.getAppID(containerID));
        String url = eurekaURL + servicePath + "/" + heartbeat.getAppID(containerID) + "/" + heartbeat.getInstanceID(containerID);
        System.out.println("DELETE " + url);

        while (true) {
            try {
                int code = HttpClient.delete(url);
                System.out.println("RESPONSE CODE: " + code);
                if (code == 200) {
                    heartbeat.removeClient(containerID);
                    break;
                }
            } catch (Exception e) {
                try{
                    System.out.println("Eureka is down. Reconnecting in 5 seconds...");
                    Thread.sleep(5000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
