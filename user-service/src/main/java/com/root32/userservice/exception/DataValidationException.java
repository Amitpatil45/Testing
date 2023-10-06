package com.root32.userservice.exception;

public class DataValidationException extends RuntimeException {
  // private static final long serialVersionUID = 1L;

  public DataValidationException(String msg) {
    super(msg);
  }
}
