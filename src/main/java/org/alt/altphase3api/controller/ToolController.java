package org.alt.altphase3api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import org.alt.altphase3api.dto.*;
import org.alt.altphase3api.exception.ErrorResponse;
import org.alt.altphase3api.service.ToolService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tools")
@Tag(name = "Tools", description = "Operations related to internal SaaS tools")
public class ToolController {

  private final ToolService toolService;

  public ToolController(ToolService toolService) {
    this.toolService = toolService;
  }

  @Operation(
      summary = "List tools",
      description = "Retrieve a paginated list of tools with optional filters and sorting")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tools retrieved successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid filter parameters"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping
  public ToolListResponse getTools(
      @Parameter(description = "Filter by department", example = "engineering")
          @RequestParam(required = false)
          Department department,
      @Parameter(description = "Filter by status", example = "active")
          @RequestParam(required = false)
          ToolStatus status,
      @Parameter(description = "Filter by category", example = "communication")
          @RequestParam(required = false)
          String category,
      @Parameter(description = "Minimum monthly cost", example = "10.00")
          @RequestParam(name = "min_cost", required = false)
          BigDecimal minCost,
      @Parameter(description = "Maximum monthly cost", example = "100.00")
          @RequestParam(name = "max_cost", required = false)
          BigDecimal maxCost,
      @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20")
          int limit,
      @Parameter(
              description =
                  "Sort format: field,direction. Allowed fields: name, cost, date ; allowed directions : asc, desc",
              example = "cost,desc")
          @RequestParam(required = false)
          String sort) {

    Pageable pageable = buildPageable(page, limit, sort);

    ToolSearchCriteria criteria =
        new ToolSearchCriteria(department, status, category, minCost, maxCost);

    return toolService.getTools(criteria, pageable);
  }

  @Operation(
      summary = "Get tool by id",
      description = "Retrieve detailed information about a specific tool")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tool found"),
    @ApiResponse(
        responseCode = "404",
        description = "Tool not found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{id}")
  public ResponseEntity<ToolDetailResponse> getTool(
      @Parameter(description = "Tool identifier", example = "42") @PathVariable Integer id) {
    return ResponseEntity.ok(toolService.getToolById(id));
  }

  @Operation(summary = "Create tool", description = "Register a new SaaS tool")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Tool created successfully"),
    @ApiResponse(
        responseCode = "400",
        description = "Validation error",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
        responseCode = "415",
        description = "Unsupported media type",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ToolResponse> createTool(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Tool creation payload",
              required = true,
              content = @Content(schema = @Schema(implementation = CreateToolRequest.class)))
          @Valid
          @RequestBody
          CreateToolRequest request) {
    ToolResponse tool = toolService.createTool(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(tool);
  }

  @Operation(
      summary = "Update tool",
      description = "Update an existing tool. Partial update is supported.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tool updated successfully"),
    @ApiResponse(
        responseCode = "400",
        description = "Validation error",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Tool not found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
        responseCode = "415",
        description = "Unsupported media type",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ToolResponse> updateTool(
      @Parameter(description = "Tool identifier", example = "42") @PathVariable Integer id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Tool update payload",
              required = true,
              content = @Content(schema = @Schema(implementation = UpdateToolRequest.class)))
          @Valid
          @RequestBody
          UpdateToolRequest request) {
    ToolResponse tool = toolService.updateTool(id, request);
    return ResponseEntity.ok(tool);
  }

  private Pageable buildPageable(int page, int limit, String sort) {

    Sort sorting = Sort.unsorted();

    if (sort != null && !sort.isBlank()) {
      String[] parts = sort.split(",");
      String field = mapSortField(parts[0]);
      Sort.Direction direction =
          parts.length > 1 && parts[1].equalsIgnoreCase("desc")
              ? Sort.Direction.DESC
              : Sort.Direction.ASC;
      sorting = Sort.by(direction, field);
    }

    return PageRequest.of(page, limit, sorting);
  }

  private String mapSortField(String field) {
    return switch (field) {
      case "cost" -> "monthlyCost";
      case "date" -> "createdAt";
      default -> "name";
    };
  }
}
