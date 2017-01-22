package io.vedder.codenapper.controller;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.vedder.codenapper.model.Status;
import io.vedder.codenapper.util.URLUtils;

@RestController
public class InputController {

  private final Logger logger = Logger.getLogger(InputController.class);

  private final Lock aLock = new ReentrantLock();

  @Autowired
  private URLUtils urlUtils;

  @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
  public Status hello() throws IOException {
    logger.info("Hello function called!");
    return new Status(true, "Hello", "");
  }

  private Status buildStandard(String branch) {
    Status status;
    aLock.lock();
    try {
      execution: {
        logger.info("build function called!");
        if (!(status = urlUtils.executeCommand("/usr/bin/hg", new String[] {"pull"}))
            .isSuccessful()) {
          status.setMessage("hg pull failed");
          break execution;
        }
        logger.info("Pull succeeded!");
        if (!(status = urlUtils.executeCommand("/usr/bin/hg", new String[] {"update", branch}))
            .isSuccessful()) {
          status.setMessage("Update to branch " + branch + " failed!");
          break execution;
        }
        logger.info("Update to branch " + branch + " succeeded!");
        if (!(status = urlUtils.executeCommand("/usr/bin/make", new String[] {"-j1"}))
            .isSuccessful()) {
          status.setMessage("make failed!");
          break execution;
        }
        logger.info("make succeeded!");
        status = new Status(true, "success - built " + branch, "");
      }
    } catch (Exception e) {
      aLock.unlock();
      return new Status(false, "Failure - Exception", e.getMessage());
    } finally {
      aLock.unlock();
    }
    return status;
  }

  private Status testStandard(String branch) {
    Status status;
    aLock.lock();
    try {
      execution: {
        logger.info("build function called!");
        if (!(status = urlUtils.executeCommand("/usr/bin/hg", new String[] {"pull"}))
            .isSuccessful()) {
          status.setMessage("hg pull failed");
          break execution;
        }
        logger.info("Pull succeeded!");
        if (!(status = urlUtils.executeCommand("/usr/bin/hg", new String[] {"update", branch}))
            .isSuccessful()) {
          status.setMessage("Update to branch " + branch + " failed!");
          break execution;
        }
        logger.info("Update to branch " + branch + " succeeded!");
        if (!(status = urlUtils.executeCommand("/usr/bin/make", new String[] {"-j1"}))
            .isSuccessful()) {
          status.setMessage("make failed!");
          break execution;
        }
        logger.info("make succeeded!");
        if (!(status = urlUtils.executeCommand("./bin/run_unit_tests", new String[] {""}))
            .isSuccessful()) {
          status.setMessage("tests failed!");
          break execution;
        }
        logger.info("tests succeeded!");
        status = new Status(true, "success - tested " + branch, "");
      }
    } catch (Exception e) {
      aLock.unlock();
      return new Status(false, "Failure - Exception", e.getMessage());
    } finally {
      aLock.unlock();
    }
    return status;
  }

  @RequestMapping(value = "/clean", method = RequestMethod.GET, produces = "application/json")
  public Status clean() throws IOException, InterruptedException {
    Status status;
    aLock.lock();
    try {
      execution: {
        logger.info("clean function called!");
        if (!(status = urlUtils.executeCommand("/usr/bin/make", new String[] {"clean"}))
            .isSuccessful()) {
          status.setMessage("make clean failed");
          break execution;
        }
        logger.info("make clean succeeded!");
        status = new Status(true, "make clean succeeded!", "");
      }
    } finally {
      aLock.unlock();
    }
    return status;
  }

  @RequestMapping(value = "/build", method = RequestMethod.GET, produces = "application/json")
  public Status build() throws IOException, InterruptedException {
    return buildStandard("default");
  }

  @RequestMapping(value = "/buildbranch/{branch}", method = RequestMethod.GET,
      produces = "application/json")
  public Status buildBranch(@PathVariable String branch) throws IOException, InterruptedException {
    logger.info("Build branch " + branch + " called!");
    return buildStandard(branch);
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
  public Status test() throws IOException, InterruptedException {
    return testStandard("default");
  }

  @RequestMapping(value = "/testbranch/{branch}", method = RequestMethod.GET,
      produces = "application/json")
  public Status testBranch(@PathVariable String branch) throws IOException, InterruptedException {
    return testStandard(branch);
  }
}
