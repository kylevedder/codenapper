package io.vedder.codenapper.model;

import org.springframework.data.annotation.Id;

public class Status {
  @Id
  private String uid;

  private boolean success;  
  private String message;
  private String output;

  public Status() {}

  public Status(boolean success, String message, String output) {
    this.success = success;
    this.message = message;
    this.output = output;
  }

  public boolean isSuccessful() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

}
