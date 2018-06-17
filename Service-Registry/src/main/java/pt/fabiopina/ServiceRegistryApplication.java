package pt.fabiopina;

import pt.fabiopina.docker.DockerConnection;
import pt.fabiopina.eureka.HeartbeatManager;
import pt.fabiopina.queue.EventQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServiceRegistryApplication {
    private static EventQueue eventQueue = new EventQueue();
    private static HeartbeatManager heartbeatManager = new HeartbeatManager(eventQueue);
    private static DockerConnection dockerClient = new DockerConnection(heartbeatManager, eventQueue);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(() -> heartbeatManager.renew());
        executorService.execute(() -> dockerClient.listenDockerEvents());
        Future<String> future = executorService.submit(() -> dockerClient.checkDockerInfo(), "200");

        try {
            String result = future.get();
            if (result.equals("200")) {
                executorService.execute(() -> dockerClient.listenQueueEvents());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
