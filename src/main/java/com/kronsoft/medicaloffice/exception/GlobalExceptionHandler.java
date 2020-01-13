package com.kronsoft.medicaloffice.exception;

import com.kronsoft.medicaloffice.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  protected ResponseEntity<Object> handleResourceAlreadyExistsException(
      ResourceAlreadyExistsException ex, WebRequest request) {
    ErrorDTO errors =
        new ErrorDTO(
            "Resource is invalid or already in use",
            HttpStatus.BAD_REQUEST.value(),
            request.getContextPath(),
            ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity<Object> handleResourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {
    ErrorDTO errors =
        new ErrorDTO(
            "Resource not Found",
            HttpStatus.NOT_FOUND.value(),
            request.getContextPath(),
            ex.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
  }

  @ExceptionHandler(ForbiddenEventException.class)
  protected ResponseEntity<Object> handleForbiddenEventException(
      ForbiddenEventException ex, WebRequest request) {
    ErrorDTO errors =
        new ErrorDTO(
            "Forbidden Event",
            HttpStatus.FORBIDDEN.value(),
            request.getContextPath(),
            ex.getMessage());

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errors);
  }

  @ExceptionHandler(InvalidInputException.class)
  protected ResponseEntity<Object> handleInvalidInputException(
          ForbiddenEventException ex, WebRequest request) {
    ErrorDTO errors =
            new ErrorDTO(
                    "Invalid input",
                    HttpStatus.BAD_REQUEST.value(),
                    request.getContextPath(),
                    ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
