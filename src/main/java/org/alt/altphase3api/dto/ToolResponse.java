package org.alt.altphase3api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ToolResponse(
    @Schema(example = "42") Integer id,
    @Schema(example = "Slack") String name,
    String description,
    @Schema(example = "Slack Technologies") String vendor,
    String websiteUrl,
    String category,
    @Schema(example = "12.50") BigDecimal monthlyCost,
    @Schema(example = "ENGINEERING") Department ownerDepartment,
    @Schema(example = "ACTIVE") ToolStatus status,
    Integer activeUsersCount,
    Instant createdAt,
    Instant updatedAt) {
  public static ToolResponse from(Tool tool) {
    return new ToolResponse(
        tool.getId(),
        tool.getName(),
        tool.getDescription(),
        tool.getVendor(),
        tool.getWebsiteUrl(),
        tool.getCategory().getName(),
        tool.getMonthlyCost(),
        tool.getOwnerDepartment(),
        tool.getStatus(),
        tool.getActiveUsersCount(),
        tool.getCreatedAt(),
        tool.getUpdatedAt());
  }
}
