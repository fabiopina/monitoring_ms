package eureka;

public class EurekaClient {

    public void register(String containerID, String image, String port, String hostname, String ipAddr) {
        System.out.println(containerID);
        System.out.println(image);
        System.out.println(port);
        System.out.println(hostname);
        System.out.println(ipAddr);
    }

    public void unregister(String containerID) {
        System.out.println(containerID);
    }
}
