package com.sample.git.exception;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

  private static final String KEY_STATUS = "status";
  private static final String KEY_MESSAGE = "message";

  /**
   * Handle Invalid Input Exceptions.
   *
   * @param exception the custom exception
   * @return the response with status and message of the exception
   */
  @ExceptionHandler(customException.class)
  public ResponseEntity<Map<String, Object>> handleInvalidInputExceptions(customException exception) {

    var body = new HashMap<String, Object>();
    var status = HttpStatus.valueOf(exception.getStatus().name());
    body.put(KEY_STATUS, exception.getStatus().name());
    body.put(KEY_MESSAGE, exception.getMessage());
    log.warn("customException occurred. {}", exception.getMessage());
    log.warn("customException content.{}, {}", HttpStatus.valueOf(exception.getStatus().name()));
    log.warn("status is : {} ", status);
    return ResponseEntity.status(status).body(body);
  }

}
