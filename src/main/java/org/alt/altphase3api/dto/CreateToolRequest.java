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
    @NotBlank @Size(min = 2, max = 100) String name,
    String description,
    @NotBlank @Size(min = 2, max = 100) String vendor,
    @URL String websiteUrl,
    @NotNull Integer categoryId,
    @NotNull @PositiveOrZero BigDecimal monthlyCost,
    @NotNull Department ownerDepartment) {

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
