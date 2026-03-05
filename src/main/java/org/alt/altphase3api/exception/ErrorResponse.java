package org.alt.altphase3api.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ErrorResponse {
  private String error;
  private String message;
  private Map<String, String> details;
}
