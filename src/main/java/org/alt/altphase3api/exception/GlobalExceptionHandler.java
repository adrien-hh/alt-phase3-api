package org.alt.altphase3api.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {

    return ErrorResponse.builder().error(ex.getError()).message(ex.getMessage()).build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {

    Map<String, String> details =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toMap(
                    error -> toSnakeCase(error.getField()),
                    FieldError::getDefaultMessage,
                    (a, b) -> a,
                    LinkedHashMap::new));

    return ErrorResponse.builder().error("Validation failed").details(details).build();
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public ErrorResponse handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {

    return ErrorResponse.builder()
        .error("Unsupported media type")
        .message("Content-Type must be application/json")
        .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleGeneric(Exception ex) {
    return ErrorResponse.builder().error("Internal server error").message(ex.getMessage()).build();
  }

  private String toSnakeCase(String fieldName) {
    return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }
}
