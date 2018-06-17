package pt.fabiopina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import pt.fabiopina.filters.post.InfoRequestFilter;
import pt.fabiopina.filters.pre.TimeTrackerFilter;
import pt.fabiopina.queue.RequestQueue;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ZuulGatewayApplication {
    static RequestQueue q = new RequestQueue();

    public static void main(String[] args) {
        q.start();
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }

    @Bean
    public TimeTrackerFilter TimeTrackerFilter() {
        return new TimeTrackerFilter();
    }

    @Bean
    public InfoRequestFilter InfoRequestFilter() {
        return new InfoRequestFilter(q);
    }

}
