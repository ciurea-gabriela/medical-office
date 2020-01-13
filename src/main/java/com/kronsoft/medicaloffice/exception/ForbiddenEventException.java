package com.kronsoft.medicaloffice.exception;

public class ForbiddenEventException extends RuntimeException {
  public ForbiddenEventException(String message) {
    super(message);
  }
}
