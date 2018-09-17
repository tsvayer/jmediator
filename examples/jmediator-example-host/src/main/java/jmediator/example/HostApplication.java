package jmediator.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "jmediator")
public class HostApplication {
  public static void main(String[] args) {
    SpringApplication.run(HostApplication.class, args);
  }
}
