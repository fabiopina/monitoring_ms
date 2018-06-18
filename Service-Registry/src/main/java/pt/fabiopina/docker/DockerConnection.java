package pt.fabiopina.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.EventsResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.fabiopina.entities.EventInfoEntity;
import pt.fabiopina.eureka.EurekaClient;
import pt.fabiopina.eureka.HeartbeatManager;
import pt.fabiopina.queue.EventQueue;

import java.util.List;

public class DockerConnection {
    private Logger logger = LoggerFactory.getLogger(DockerConnection.class);
    private DockerClient dockerClient = null;
    private HeartbeatManager heartbeatManager;
    private EventQueue eventQueue;

    public DockerConnection(HeartbeatManager heartbeatManager, EventQueue queue) {
        this.heartbeatManager = heartbeatManager;
        this.eventQueue = queue;
    }

    public DockerClient getClient() {
        if(dockerClient != null) return dockerClient;

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .build();

        return DockerClientBuilder.getInstance(config).build();
    }

    public void listenDockerEvents() {
        logger.info("Listening to docker events ...");
        try {
            getClient().eventsCmd().exec(callback).awaitCompletion().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventsResultCallback callback = new EventsResultCallback() {
        @Override
        public void onNext(Event event) {
            if (event.getAction().equals("start") || event.getAction().equals("stop")) {
                if (getClient().inspectContainerCmd(event.getId()).exec().getConfig().getImage().contains("registerwitheureka_")) {

                    ListNetworksCmd networksCmd = getClient().listNetworksCmd();
                    List<Network> listNetworks = networksCmd.exec();

                    String containerID = event.getId();
                    String image = getClient().inspectContainerCmd(containerID).exec().getConfig().getImage();
                    String port = getClient().inspectContainerCmd(containerID).exec().getConfig().getExposedPorts()[0].toString();
                    if (port.contains("/")) port = port.split("/")[0];
                    String ipAddr = null;
                    for (Network network: listNetworks) {
                        if (getClient().inspectContainerCmd(containerID).exec().getNetworkSettings().getNetworks().containsKey(network.getName())) {
                            ipAddr = getClient().inspectContainerCmd(containerID).exec().getNetworkSettings().getNetworks().get(network.getName()).getIpAddress();
                        }
                    }
                    eventQueue.addEvent(new EventInfoEntity(event.getAction(), containerID, image, ipAddr, port));
                }
            }
            super.onNext(event);
        }
    };

    public String checkDockerInfo() {
        logger.info("Checking for containers already running ...");

        ListContainersCmd listContainersCmd = getClient().listContainersCmd().withStatusFilter("running");
        List<Container> listContainers = listContainersCmd.exec();

        ListNetworksCmd networksCmd = getClient().listNetworksCmd();
        List<Network> listNetworks = networksCmd.exec();

        for (Container c: listContainers) {

            if (c.getImage().contains("registerwitheureka_")) {
                String containerID = c.getId();
                String image = c.getImage();
                String port = c.getPorts()[0].getPrivatePort()+"";
                String ipAddr = null;
                for (Network network: listNetworks) {
                    if (c.getNetworkSettings().getNetworks().containsKey(network.getName())) {
                        c.getNetworkSettings().getNetworks().get(network.getName()).getIpAddress();
                    }
                }
                new EurekaClient(heartbeatManager, new EventInfoEntity("start", containerID, image, ipAddr, port)).startRegistrationProcess();
            }
        }
        return "200";
    }

    public void listenQueueEvents() {
        logger.info("Listening for events in queue ...");

        while (true) {
            try {
                EventInfoEntity event = eventQueue.getEvent();
                if (event.getEvent().equals("start")) {
                    new EurekaClient(heartbeatManager, new EventInfoEntity("start", event.getContainerID(), event.getImage(), event.getIpAddr(), event.getPort())).startRegistrationProcess();
                }
                if (event.getEvent().equals("stop")) {
                    new EurekaClient(heartbeatManager, event.getContainerID()).deregister();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
