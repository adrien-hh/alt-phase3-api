package org.alt.altphase3api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ToolResponse(
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
        Instant createdAt,
        Instant updatedAt
) {
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
                tool.getUpdatedAt()
        );
    }
}