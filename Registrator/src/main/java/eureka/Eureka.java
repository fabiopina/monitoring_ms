package eureka;

public class Eureka {

    public void register(HeartbeatManager heartbeat, String containerID, String image, String port, String hostname, String ipAddr) {

        if (image.contains("/")) image = image.substring(image.indexOf("/")+1).trim();
        if (image.contains(":")) image = image.split(":")[0];
        image = image.replace("registerwitheureka_", "");

        new EurekaClient(heartbeat, containerID, image, hostname, ipAddr, port).startRegistrationProcess();

    }

    public void unregister(HeartbeatManager heartbeat, String containerID) {
        new EurekaClient().deregister(heartbeat, containerID);
    }
}
