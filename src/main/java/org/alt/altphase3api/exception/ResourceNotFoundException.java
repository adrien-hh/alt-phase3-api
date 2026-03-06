package org.alt.altphase3api.exception;

import lombok.Getter;

/** Thrown when a requested resource is not found. */
@Getter
public class ResourceNotFoundException extends BusinessException {

  private final String resourceName;
  private final Object resourceId;

  public ResourceNotFoundException(String resourceName, Object resourceId) {
    super(
        String.format("%s not found", resourceName),
        String.format("%s with ID %s does not exist", resourceName, resourceId));
    this.resourceName = resourceName;
    this.resourceId = resourceId;
  }
}
