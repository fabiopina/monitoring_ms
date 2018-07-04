package pt.fabiopina.queue;

import pt.fabiopina.database.MysqlClient;
import pt.fabiopina.entities.CleanInfoEntity;
import pt.fabiopina.entities.RawInfoEntity;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RequestQueue {
    private BlockingQueue<RawInfoEntity> requestQueue;
    private MysqlClient mysqlClient;

    public RequestQueue() {
        requestQueue = new LinkedBlockingDeque<>();
        mysqlClient = new MysqlClient();
        mysqlClient.createTable();
    }

    public void add(RawInfoEntity element) {
        requestQueue.add(element);
    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                while (true) {
                    try{
                        RawInfoEntity element = requestQueue.take();
                        processElement(element);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }});
        thread.start();
    }

    public void processElement(RawInfoEntity element) {
        String destinyMicroservice = element.getRequestURL().split("/")[3];
        String destinyInstanceandPort = element.getInstance().split("/")[0];

        String url = null;
        if(element.getInstance().indexOf('/') == -1) {
            url = "/";
        }
        else {
            url = element.getInstance().substring(element.getInstance().indexOf("/")+1).trim();
            url = "/" + url;
        }

        String destinyIp = null;
        try{
            // Get IP given instance name
            InetAddress addr = InetAddress.getByName(destinyInstanceandPort.split(":")[0]);
            destinyIp = addr.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mysqlClient.addEntry(new CleanInfoEntity(new Timestamp(element.getStartTime()), new Timestamp(element.getEndTime()), element.getStartTime(), element.getEndTime(), element.getEndTime() - element.getStartTime(), element.getRemoteAddr(), destinyMicroservice, destinyInstanceandPort.split(":")[0], destinyIp, element.getMethod(), url, ""+element.getRemotePort(), destinyInstanceandPort.split(":")[1], ""+element.getStatusCode()));
    }
}
