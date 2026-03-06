package org.alt.altphase3api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Schema(description = "Usage metrics for the last 30 days")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UsageMetrics(@JsonProperty("last_30_days") Last30Days last30Days) {
  public static UsageMetrics from(long totalSessions, int avgMinutes) {
    return new UsageMetrics(new Last30Days(totalSessions, avgMinutes));
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Last30Days(long totalSessions, int avgSessionMinutes) {}
}
