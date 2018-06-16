package docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.EventsResultCallback;
import eureka.Eureka;
import eureka.HeartbeatManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerConnection {
    private static Logger logger = LoggerFactory.getLogger(DockerConnection.class);
    private static DockerClient dockerClient = null;
    public static HeartbeatManager hearbeat;

    private static DockerClient getClient() {
        if(dockerClient != null) return dockerClient;

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .build();

        dockerClient = DockerClientBuilder.getInstance(config).build();
        return dockerClient;
    }

    public static void listenEvents() {
        try {
            getClient().eventsCmd().exec(callback).awaitCompletion().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static EventsResultCallback callback = new EventsResultCallback() {
        @Override
        public void onNext(Event event) {
            if (event.getAction().equals("start")) {
                if (getClient().inspectContainerCmd(event.getId()).exec().getConfig().getImage().contains("registerwitheureka_")) {

                    String containerID = event.getId();
                    String image = getClient().inspectContainerCmd(containerID).exec().getConfig().getImage();
                    String port = getClient().inspectContainerCmd(containerID).exec().getConfig().getExposedPorts()[0].toString();
                    if (port.contains("/")) port = port.split("/")[0];
                    String hostname = getClient().inspectContainerCmd(containerID).exec().getConfig().getHostName();
                    String networkName = getClient().inspectContainerCmd(containerID).exec().getHostConfig().getNetworkMode();
                    String ipAddr = getClient().inspectContainerCmd(containerID).exec().getNetworkSettings().getNetworks().get(networkName).getIpAddress();

                    new Eureka().register(hearbeat, containerID, image, port, hostname, ipAddr);
                }

            }

            if (event.getAction().equals("stop")) {
                if (getClient().inspectContainerCmd(event.getId()).exec().getConfig().getImage().contains("registerwitheureka_")) {

                    String containerID = event.getId();

                    new Eureka().unregister(hearbeat, containerID);
                }
            }

            super.onNext(event);
        }
    };

    public static void run() {
        logger.info("Starting heartbeat ...");
        hearbeat = new HeartbeatManager();
        hearbeat.start();
        logger.info("Listening for docker events ...");
        listenEvents();
    }
}

