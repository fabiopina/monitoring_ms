package hello;

import hello.http.RequestQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import hello.filters.pre.LogIncomingRequest;
import hello.filters.post.LogLeavingRequest;

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
  public LogIncomingRequest logIncomingRequest() {
    return new LogIncomingRequest(q);
  }

  @Bean
  public LogLeavingRequest logLeavingRequest() {
    return new LogLeavingRequest(q);
  }

}
