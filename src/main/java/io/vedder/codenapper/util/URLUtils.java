package io.vedder.codenapper.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class URLUtils {

  /**
   * Takes a URL and URI and appends them appropriately, regardless of leading and trailing slashes.
   * 
   * @param url Base URL
   * @param uri URI to append
   * @return
   */
  public static String appendURI(String url, String uri) {
    if (!url.endsWith("/") && !uri.startsWith("/"))
      url = url.concat("/");
    if (url.endsWith("/") && uri.startsWith("/"))
      uri = uri.substring(1);
    url = url.concat(uri);
    return url;
  }

  public static String executeCommand(String executable, String[] command) throws InterruptedException, IOException {
    // Build command
    List<String> commands = new ArrayList<String>();
    commands.add(executable);
    // Add arguments
    commands.addAll(Arrays.asList(command));
    System.out.println(commands);

    // Run macro on target
    ProcessBuilder pb = new ProcessBuilder(commands);
    pb.directory(new File("/home/ubuntu/code/robocup-ssl/"));
    pb.redirectErrorStream(true);
    Process process = pb.start();

    // Read output
    StringBuilder out = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null, previous = null;
    while ((line = br.readLine()) != null)
      if (!line.equals(previous)) {
        previous = line;
        out.append(line).append('\n');
        System.out.println(line);
      }

    // Check result
    if (process.waitFor() == 0) {
      System.out.println("Success!");
      return out.toString();
    } else {
      // Abnormal termination: Log command parameters and output and throw ExecutionException
      System.err.println(commands);
      System.err.println(out.toString());
      return null;
    }
  }

}
