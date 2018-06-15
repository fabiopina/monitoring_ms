package eureka;

public class Eureka {

    public void register(String containerID, String image, String port, String hostname, String ipAddr) {

        if (image.contains("/")) image = image.substring(image.indexOf("/")+1).trim();
        if (image.contains(":")) image = image.split(":")[0];
        image = image.replace("registerwitheureka_", "");

        System.out.println(containerID);
        System.out.println(image);
        System.out.println(hostname);
        System.out.println(ipAddr);
        System.out.println(port);

        new EurekaClient(containerID, image, hostname, ipAddr, port).startRegistrationProcess();

    }

    public void unregister(String containerID) {
        System.out.println(containerID);
    }
}
