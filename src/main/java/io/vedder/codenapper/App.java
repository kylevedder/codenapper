package io.vedder.codenapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main app, launches the application and points Spring to the appropriate packages to scan.
 * 
 * @author kyle
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "io.vedder.codenapper")
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
