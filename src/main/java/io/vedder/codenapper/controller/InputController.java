package io.vedder.codenapper.controller;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.vedder.codenapper.util.URLUtils;

@RestController
public class InputController {

  private static final Logger logger = Logger.getLogger(InputController.class);

  private static final Lock aLock = new ReentrantLock();

  @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
  public String hello() throws IOException {
    logger.info("Hello function called!");
    return "Hello!";
  }

  private static String buildStandard(String branch) {
    String message = "";
    aLock.lock();
    try {
      execution: {
        logger.info("build function called!");
        if (URLUtils.executeCommand("/usr/bin/hg", new String[] {"pull"}) == null) {
          message = "Pull failed!";
          break execution;
        }
        logger.info("Pull succeeded!");
        if (URLUtils.executeCommand("/usr/bin/hg", new String[] {"update", branch}) == null) {
          message = "Update to branch " + branch + " failed!";
          break execution;
        }
        logger.info("Update to branch " + branch + " succeeded!");
        if (URLUtils.executeCommand("/usr/bin/make", new String[] {"-j1"}) == null) {
          message = "make failed!";
          break execution;
        }
        logger.info("make succeeded!");
        message = "success - built " + branch;
      }
    } catch (Exception e) {
      aLock.unlock();
      return "failure - exception thrown";
    } finally {
      aLock.unlock();
    }
    return message;
  }

  private static String testStandardv2(String branch) {
    String message = "";
    aLock.lock();
    try {
      execution: {
        logger.info("build function called!");
        if (URLUtils.executeCommand("/usr/bin/hg", new String[] {"pull"}) == null) {
          message = "Pull failed!";
          break execution;
        }
        logger.info("Pull succeeded!");
        if (URLUtils.executeCommand("/usr/bin/hg", new String[] {"update", branch}) == null) {
          message = "Update to branch " + branch + " failed!";
          break execution;
        }
        logger.info("Update to branch " + branch + " succeeded!");
        if (URLUtils.executeCommand("/usr/bin/make", new String[] {"-j1"}) == null) {
          message = "make failed!";
          break execution;
        }
        logger.info("make succeeded!");
        if (URLUtils.executeCommand("./bin/run_unit_tests", new String[] {""}) == null) {
          message = "tests failed!";
        }
        logger.info("tests succeeded!");
        message = "success - tested " + branch;
      }
    } catch (Exception e) {
      aLock.unlock();
      return "failure - exception thrown";
    } finally {
      aLock.unlock();
    }
    return message;
  }

  @RequestMapping(value = "/clean", method = RequestMethod.GET, produces = "application/json")
  public String clean() throws IOException, InterruptedException {
    aLock.lock();
    try {
      logger.info("clean function called!");
      if (URLUtils.executeCommand("/usr/bin/make", new String[] {"clean"}) == null) {
        return "make clean failed!";
      }
      logger.info("make clean succeeded!");
      return "success - clean";
    } finally {
      aLock.unlock();
    }
  }

  @RequestMapping(value = "/build", method = RequestMethod.GET, produces = "application/json")
  public String build() throws IOException, InterruptedException {
    return buildStandard("default");
  }

  @RequestMapping(value = "/buildbranch/{branch}", method = RequestMethod.GET,
      produces = "application/json")
  public String buildBranch(@PathVariable String branch) throws IOException, InterruptedException {
    logger.info("Build branch " + branch + " called!");
    return buildStandard(branch);
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
  public String test() throws IOException, InterruptedException {
    return testStandardv2("default");
  }

  @RequestMapping(value = "/testbranch/{branch}", method = RequestMethod.GET,
      produces = "application/json")
  public String testBranch(@PathVariable String branch) throws IOException, InterruptedException {
    return testStandardv2(branch);
  }
}
