package io.vedder.codenapper.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import io.vedder.codenapper.model.Status;

@Component
public class URLUtils {
  @Value("${source-path}")
  private String sourcePath;

  private static final Logger logger = Logger.getLogger(URLUtils.class);

  public Status executeCommand(String executable, String[] command)
      throws InterruptedException, IOException {
    // Build command
    List<String> commands = new ArrayList<String>();
    commands.add(executable);
    // Add arguments
    commands.addAll(Arrays.asList(command));
    System.out.println(commands);

    // Run macro on target
    ProcessBuilder pb = new ProcessBuilder(commands);
    System.out.println("Source path: " + sourcePath);
    pb.directory(new File(sourcePath));
    pb.redirectErrorStream(true);
    Process process = pb.start();

    // Read output
    StringBuilder out = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null;
    String previous = null;
    while ((line = br.readLine()) != null) {
      if (!line.equals(previous)) {
        previous = line;
        out.append(line).append('\n');
        logger.info(line);
      }
    }

    return new Status(process.waitFor() == 0, null, out.toString());
  }

  // To resolve ${} in @Value
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
    return new PropertySourcesPlaceholderConfigurer();
  }

}
