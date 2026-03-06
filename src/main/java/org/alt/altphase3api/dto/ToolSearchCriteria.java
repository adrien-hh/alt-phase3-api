package org.alt.altphase3api.dto;

import java.math.BigDecimal;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;

public record ToolSearchCriteria(
    Department department,
    ToolStatus status,
    String category,
    BigDecimal minCost,
    BigDecimal maxCost) {}
