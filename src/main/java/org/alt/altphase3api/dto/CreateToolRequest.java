package org.alt.altphase3api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.alt.altphase3api.domain.bo.Category;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.domain.enums.Department;
import org.hibernate.validator.constraints.URL;

public record CreateToolRequest(
    @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name is required and must be {min}-{max} characters")
        String name,
    String description,
    @NotBlank(message = "Vendor is required")
        @Size(min = 2, max = 100, message = "Vendor is required and must be {min}-{max} characters")
        String vendor,
    @URL(message = "Must be a valid URL format") String websiteUrl,
    @NotNull(message = "Category ID is required") Integer categoryId,
    @NotNull(message = "Monthly cost is required")
        @PositiveOrZero(message = "Must be a positive number")
        BigDecimal monthlyCost,
    @NotNull(message = "Owner department is required") Department ownerDepartment) {

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
