package org.alt.altphase3api.dto;

import java.util.List;
import java.util.Map;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ToolListResponse(
    List<ToolResponse> data,
    long total,
    long filtered,
    Map<String, Object> filtersApplied
) {}
