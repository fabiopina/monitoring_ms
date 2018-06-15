package eureka;

import http.HttpClient;

public class EurekaClient {
    private String eurekaURL, servicePath, hearbeatInterval, containerID, appName, hostName, ipAddr, port;

    public EurekaClient(String containerID, String appName, String hostName, String ipAddr, String port) {
        this.containerID = containerID;
        this.appName = appName;
        this.hostName = hostName;
        this.ipAddr = ipAddr;
        this.port = port;
        this.eurekaURL = "http://" + System.getenv("EUREKA") + "/eureka/";
        this.servicePath = System.getenv("SERVICEPATH");
        this.hearbeatInterval = System.getenv("HEARTBEATINTERVAL");
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
    }

    public void register() {
        String url = eurekaURL + servicePath + "/" + appName;

        while (true) {
            try {
                int code = HttpClient.post(url, getInstanceData());
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


}
