package org.alt.altphase3api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UsageMetrics(@JsonProperty("last_30_days") Last30Days last30Days) {
  public static UsageMetrics from(long totalSessions, int avgMinutes) {
    return new UsageMetrics(new Last30Days(totalSessions, avgMinutes));
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Last30Days(long totalSessions, int avgSessionMinutes) {}
}
