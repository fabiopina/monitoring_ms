package hello.queue;

public class CtxInfoObject {
    private String remoteAddr, method, requestURL, instance;
    private int remotePort;
    private long startTime, endTime;

    public CtxInfoObject() {}

    public static CtxInfoObject constructObjectFromCtx(long startTime, long endTime, String remoteAddr, String method, String requestURL, String instance, int remotePort) {
        CtxInfoObject c = new CtxInfoObject();
        c.setStartTime(startTime);
        c.setEndTime(endTime);
        c.setRemoteAddr(remoteAddr);
        c.setMethod(method);
        c.setRequestURL(requestURL);
        c.setInstance(instance);
        c.setRemotePort(remotePort);

        return c;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
}
