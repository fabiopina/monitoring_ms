package hello.queue;

import hello.database.DatabaseConnection;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RequestQueue {
    BlockingQueue<CtxInfoObject> requestQueue;
    DatabaseConnection db;
    Connection conn;

    public RequestQueue() {
        requestQueue = new LinkedBlockingDeque<>();
        db = new DatabaseConnection();
        conn = db.connect();
        db.createTable(conn);
    }

    public void add(CtxInfoObject element) {
        requestQueue.add(element);
    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                while (true) {
                    try{
                        CtxInfoObject element = requestQueue.take();
                        processRequest(element);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }});
        thread.start();
    }

    public void processRequest(CtxInfoObject c) {

        String destinyMicroservice = c.getRequestURL().split("/")[3];
        String destinyInstance = c.getInstance().split("/")[0];

        String path = null;
        if(c.getInstance().indexOf('/') == -1) {
            path = "/";
        }
        else {
            path = c.getInstance().substring(c.getInstance().indexOf("/")+1).trim();
        }
        String destinyFunction = c.getMethod() + " -> " + path;

        String destinyIp = null;
        try{
            // Get IP given instance name
            InetAddress addr = InetAddress.getByName(destinyInstance.split(":")[0]);
            destinyIp = addr.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }


        db.addEntry(conn, c.getStartTime(), c.getEndTime(), c.getEndTime() - c.getStartTime(), c.getRemoteAddr(), c.getRemotePort(), destinyMicroservice, destinyInstance, destinyIp, destinyFunction);
    }
}
