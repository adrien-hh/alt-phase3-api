package org.alt.altphase3api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import lombok.Builder;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import org.hibernate.validator.constraints.URL;

@Builder
public record UpdateToolRequest(
        @Size(min = 2, max = 100) String name,
        String description,
        @Size(min = 2, max = 100) String vendor,
        @URL String websiteUrl,
        Integer categoryId,
        @PositiveOrZero @Digits(integer = 8, fraction = 2) BigDecimal monthlyCost,
        Department ownerDepartment,
        ToolStatus status) {}
