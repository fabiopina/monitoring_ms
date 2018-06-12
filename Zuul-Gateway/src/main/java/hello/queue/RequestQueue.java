package hello.queue;

import com.netflix.client.IResponse;
import com.netflix.zuul.context.RequestContext;
import hello.database.DatabaseConnection;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RequestQueue {
    BlockingQueue<RequestContext> requestQueue;
    DatabaseConnection db;
    Connection conn;

    public RequestQueue() {
        requestQueue = new LinkedBlockingDeque<>();
        db = new DatabaseConnection();
        conn = db.connect();
        db.createTable(conn);
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
        String path = request.getRequestURL().toString().split("/")[3] + "/";
        String destinyMicroservice = path.split("/")[0];
        String destinyInstance = ((IResponse) ctx.get("ribbonResponse")).getRequestedURI().toString().substring(7);
        String destinyFunction = request.getMethod() + " -> " + path;

        String destinyIp = null;
        long diff = 0;

        try{
            // Get IP given instance name
            InetAddress addr = InetAddress.getByName(destinyInstance.split(":")[0]);
            destinyIp = addr.getHostAddress();

            // Get diference between 2 timestamps
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date startParsedDate = dateFormat.parse(timeStart);
            Date endParsedDate = dateFormat.parse(timeEnd);
            diff = endParsedDate.getTime() - startParsedDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.addEntry(conn, timeStart, timeEnd, ""+diff, sourceIp, ""+sourcePort, destinyMicroservice, destinyInstance, destinyIp, destinyFunction);
    }
}
