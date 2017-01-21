package io.vedder.codenapper.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.vedder.codenapper.util.URLUtils;

@RestController
public class InputController {

  private static final Logger logger = Logger.getLogger(InputController.class);

  @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
  public String hello() throws IOException {
    logger.info("Hello function called!");
    return "Hello!";
  }

  @RequestMapping(value = "/build", method = RequestMethod.GET, produces = "application/json")
  public String build() throws IOException, InterruptedException {
    logger.info("build function called!");
    if (URLUtils.executeCommand("/usr/bin/hg", new String[] {"pull"}) == null) {
      return "Pull failed!";
    }
    logger.info("Pull succeeded!");
    if (URLUtils.executeCommand("/usr/bin/hg", new String[] {"update", "default"}) == null) {
      return "Update failed!";
    }
    logger.info("Update succeeded!");
    if (URLUtils.executeCommand("/usr/bin/make", new String[] {"-j1"}) == null) {
      return "make failed!";
    }
    logger.info("make succeeded!");
    return "success - build";
  }

  @RequestMapping(value = "/buildbranch/{branch}", method = RequestMethod.GET,
      produces = "application/json")
  public String buildBranch(@PathVariable String branch) throws IOException, InterruptedException {
    logger.info("build function called!");
    if (URLUtils.executeCommand("/usr/bin/hg", new String[]{"pull"}) == null) {
      return "Pull failed!";
    }
    logger.info("Pull succeeded!");
    if (URLUtils.executeCommand("/usr/bin/hg", new String[]{"update", branch}) == null) {
      return "Update to branch " + branch + " failed!";
    }
    logger.info("Update to branch " + branch + " succeeded!");
    if (URLUtils.executeCommand("/usr/bin/make", new String[]{"-j1"}) == null) {
      return "make failed!";
    }
    logger.info("make succeeded!");
    return "success - build";
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
  public String test() throws IOException, InterruptedException {
    logger.info("test function called!");
    if (URLUtils.executeCommand("/usr/bin/hg", new String[]{"pull"}) == null) {
      return "Pull failed!";
    }
    logger.info("Pull succeeded!");
    if (URLUtils.executeCommand("/usr/bin/hg", new String[]{"updat", "default"}) == null) {
      return "Update failed!";
    }
    logger.info("Update succeeded!");
    if (URLUtils.executeCommand("/usr/bin/make", new String[]{"-j1"}) == null) {
      return "make failed!";
    }
    logger.info("make succeeded!");
    if (URLUtils.executeCommand("./bin/run_unit_tests", new String[]{""}) == null) {
      return "tests failed!";
    }
    return "success - test";
  }

  @RequestMapping(value = "/testbranch/{branch}", method = RequestMethod.GET,
      produces = "application/json")
  public String testBranch(@PathVariable String branch) throws IOException, InterruptedException {
    logger.info("build function called!");
    if (URLUtils.executeCommand("/usr/bin/hg", new String[]{"pull"}) == null) {
      return "Pull failed!";
    }
    logger.info("Pull succeeded!");
    if (URLUtils.executeCommand("/usr/bin/hg", new String[]{"update", branch}) == null) {
      return "Update to branch " + branch + " failed!";
    }
    logger.info("Update to branch " + branch + " succeeded!");
    if (URLUtils.executeCommand("/usr/bin/make", new String[]{"-j1"}) == null) {
      return "make failed!";
    }
    logger.info("make succeeded!");
    if (URLUtils.executeCommand("./bin/run_unit_tests", new String[]{""}) == null) {
      return "tests failed!";
    }
    return "success - build";
  }
}
