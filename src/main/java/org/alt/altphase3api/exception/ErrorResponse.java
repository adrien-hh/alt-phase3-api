package org.alt.altphase3api.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonPropertyOrder({ "error", "message", "details" })
public class ErrorResponse {

  @Schema(example = "Tool not found")
  private String error;

  @Schema(example = "Tool with ID 999 does not exist")
  private String message;

  @Schema(description = "Field validation errors")
  private Map<String, String> details;
}


