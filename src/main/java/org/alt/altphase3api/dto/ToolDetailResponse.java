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
public record ToolDetailResponse(
        Integer id,
        String name,
        String description,
        String vendor,
        String websiteUrl,
        String category,
        BigDecimal monthlyCost,
        Department ownerDepartment,
        ToolStatus status,
        Integer activeUsersCount,
        @Schema(example = "1250.00")
        BigDecimal totalMonthlyCost,
        Instant createdAt,
        Instant updatedAt,
        @Schema(description = "Usage statistics for the last 30 days")
        UsageMetrics usageMetrics
        ) {

    public static ToolDetailResponse from(Tool tool, long totalSessions, int avgMinutes) {

        ToolResponse base = ToolResponse.from(tool);

        BigDecimal totalCost =
                tool.getMonthlyCost()
                        .multiply(BigDecimal.valueOf(tool.getActiveUsersCount()));

        UsageMetrics metrics = UsageMetrics.from(totalSessions, avgMinutes);

        return new ToolDetailResponse(
                base.id(),
                base.name(),
                base.description(),
                base.vendor(),
                base.websiteUrl(),
                base.category(),
                base.monthlyCost(),
                base.ownerDepartment(),
                base.status(),
                base.activeUsersCount(),
                totalCost,
                base.createdAt(),
                base.updatedAt(),
                metrics
                );
    }
}
