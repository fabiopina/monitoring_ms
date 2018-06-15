package entity;

public class InfoClient {
    String appID, instanceID, hostname, ipAddr, port;

    public static InfoClient constructInfoClient(String appID, String instanceID, String hostname, String ipAddr, String port) {
        InfoClient c = new InfoClient();
        c.setAppID(appID);
        c.setInstanceID(instanceID);
        c.setHostname(hostname);
        c.setIpAddr(ipAddr);
        c.setPort(port);

        return c;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
