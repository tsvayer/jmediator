package jmediator.gateway;

import jmediator.MessageBus;
import jmediator.MessageRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
public class Installer {
  @Autowired
  private SparkjavaGateway gateway;
  @Autowired
  private Environment env;

  @Bean
  SparkjavaGateway gateway(
    ObjectMapper objectMapper,
    MessageBus messageBus,
    MessageRegistry messageRegistry) {
    return new SparkjavaGateway(objectMapper, messageBus, messageRegistry);
  }

  @PostConstruct
  void configure() {
    var port = env.getProperty("server.port", Integer.class);
    new RouteConfiguration().configure(gateway, port);
  }
}
