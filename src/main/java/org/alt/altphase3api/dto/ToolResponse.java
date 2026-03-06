package org.alt.altphase3api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record ToolResponse(
        Integer id,
        String name,
        String description,
        String vendor,
        @JsonProperty("website_url") String websiteUrl,
        String category,
        @JsonProperty("monthly_cost") BigDecimal monthlyCost,
        @JsonProperty("owner_department") Department ownerDepartment,
        ToolStatus status,
        @JsonProperty("active_users_count") Integer activeUserCount,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
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