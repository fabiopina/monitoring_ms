package hello;

import hello.queue.RequestQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import hello.filters.pre.TimeTrackerFilter;
import hello.filters.post.InfoRequestFilter;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {
  static RequestQueue q = new RequestQueue();

  public static void main(String[] args) {
    q.start();
    SpringApplication.run(GatewayApplication.class, args);
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
