package pt.fabiopina.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

@Configuration
public class EurekaClientConfig {

    @Bean
    @Autowired
    @Profile("docker")
    public EurekaInstanceConfigBean eurekaInstanceConfig(final InetUtils inetUtils) throws IOException {
        final String hostName = System.getenv("HOSTNAME");
        System.out.println("HOSTNAME: " + hostName);
        String hostAddress = null;

        final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netInt : Collections.list(networkInterfaces)) {
            for (InetAddress inetAddress : Collections.list(netInt.getInetAddresses())) {
                if (hostName.equals(inetAddress.getHostName())) {
                    hostAddress = inetAddress.getHostAddress();
                    System.out.println("IP ADDRESS: " + hostAddress);
                }

                System.out.printf("%s: %s / %s\n", netInt.getName(),  inetAddress.getHostName(), inetAddress.getHostAddress());
            }
        }

        if (hostAddress == null) {
            throw new UnknownHostException("Cannot find ip address for hostname: " + hostName);
        }

        final EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        config.setHostname(hostName);
        config.setIpAddress(hostAddress);
        return config;
    }
}