package org.alt.altphase3api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Builder;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import org.hibernate.validator.constraints.URL;

@Builder
public record UpdateToolRequest(
    @Schema(example = "Slack") @Size(min = 2, max = 100) String name,
    @Schema(example = "Team communication platform") String description,
    @Schema(example = "Slack Technologies") @Size(min = 2, max = 100) String vendor,
    @URL(message = "Must be a valid URL format")
        @Pattern(regexp = "^(https?://).+", message = "Must be a valid URL format")
        @Schema(example = "https://slack.com")
        String websiteUrl,
    @Schema(example = "3") Integer categoryId,
    @PositiveOrZero @Digits(integer = 8, fraction = 2) @Schema(example = "12.50")
        BigDecimal monthlyCost,
    @Schema(example = "Engineering") Department ownerDepartment,
    @Schema(example = "active") ToolStatus status) {}
