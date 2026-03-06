package org.alt.altphase3api.exception;

import lombok.Getter;

/**
 * Base class for business logic exceptions.
 *
 * <p>All custom business exceptions should extend this class to provide consistent error handling
 * with error codes for client-side processing.
 */
@Getter
public abstract class BusinessException extends RuntimeException {

  private final String error;

  protected BusinessException(String error, String message) {
    super(message);
    this.error = error;
  }

  protected BusinessException(String error, String message, Throwable cause) {
    super(message, cause);
    this.error = error;
  }
}
