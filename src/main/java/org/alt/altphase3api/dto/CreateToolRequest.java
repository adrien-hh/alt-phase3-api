package org.alt.altphase3api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

import lombok.Builder;
import org.alt.altphase3api.domain.bo.Category;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.domain.enums.Department;
import org.hibernate.validator.constraints.URL;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CreateToolRequest(
    @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name is required and must be {min}-{max} characters")
    @Schema(example = "Slack")
        String name,
    @Schema(example = "Team communication platform")
    String description,
    @NotBlank(message = "Vendor is required")
        @Size(min = 2, max = 100, message = "Vendor is required and must be {min}-{max} characters")
    @Schema(example = "Slack Technologies")
        String vendor,
    @URL(message = "Must be a valid URL format")
        @Pattern(regexp = "^(https?://).+", message = "Must be a valid URL format")
    @Schema(example = "https://slack.com")
        String websiteUrl,
    @NotNull(message = "Category ID is required")
    @Schema(example = "3")
    Integer categoryId,
    @NotNull(message = "Monthly cost is required")
        @PositiveOrZero(message = "Must be a positive number")
    @Schema(example = "12.50")
        BigDecimal monthlyCost,
    @NotNull(message = "Owner department is required")
    @Schema(example = "Engineering")
    Department ownerDepartment) {

  public Tool toTool(Category category) {
    return Tool.builder()
        .name(name())
        .description(description())
        .vendor(vendor())
        .websiteUrl(websiteUrl())
        .category(category)
        .monthlyCost(monthlyCost())
        .ownerDepartment(ownerDepartment())
        .build();
  }
}
