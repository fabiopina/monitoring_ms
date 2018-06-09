package hello.http;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RequestQueue {
    BlockingQueue<String> requestQueue;

    public RequestQueue() {
        requestQueue = new LinkedBlockingDeque<>();
    }

    public void add(String element) {
        requestQueue.add(element);
    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                try{
                    while (true) {
                        String element = requestQueue.take();
                        HttpRequest.post(element);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }


            }});
        thread.start();
    }

}
