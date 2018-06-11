package hello.queue;

import com.netflix.client.IResponse;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RequestQueue {
    BlockingQueue<RequestContext> requestQueue;

    public RequestQueue() {
        requestQueue = new LinkedBlockingDeque<>();
    }

    public void add(RequestContext element) {
        requestQueue.add(element);
    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                try{
                    while (true) {
                        RequestContext element = requestQueue.take();
                        processRequest(element);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }


            }});
        thread.start();
    }

    public void processRequest(RequestContext ctx) {
        HttpServletRequest request = ctx.getRequest();

        String timeStart = ctx.getZuulRequestHeaders().get("time-start");
        String timeEnd = ctx.getZuulRequestHeaders().get("time-end");
        String sourceIp = request.getRemoteAddr();
        int sourcePort = request.getRemotePort();
        String destinyMicroservice = ctx.getZuulRequestHeaders().get("x-forwarded-prefix").substring(1);
        String destinyInstance = ((IResponse) ctx.get("ribbonResponse")).getRequestedURI().toString().substring(7);
        String destinyIp = null;
        String destinyFunction = request.getMethod() + " -> " + request.getRequestURL().toString().split("/")[3];

        try{
            InetAddress addr = InetAddress.getByName(destinyInstance.split(":")[0]);
            destinyIp = addr.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(timeStart);
        System.out.println(timeEnd);
        System.out.println(sourceIp);
        System.out.println(sourcePort);
        System.out.println(destinyMicroservice);
        System.out.println(destinyInstance);
        System.out.println(destinyIp);
        System.out.println(destinyFunction);
    }

}
