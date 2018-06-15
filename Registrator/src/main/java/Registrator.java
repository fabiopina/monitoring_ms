import docker.DockerConnection;

public class Registrator {
    public static void main(String[] args) {
        System.out.println("Starting hearbeat manager");
        System.out.println("Listening for docker events...");
        DockerConnection.listenEvents();
    }
}
