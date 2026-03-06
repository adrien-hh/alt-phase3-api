package org.alt.altphase3api.controller;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import org.alt.altphase3api.dto.*;
import org.alt.altphase3api.service.ToolService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

  private final ToolService toolService;

  public ToolController(ToolService toolService) {
    this.toolService = toolService;
  }

  @GetMapping
  public ToolListResponse getTools(
      @RequestParam(required = false) Department department,
      @RequestParam(required = false) ToolStatus status,
      @RequestParam(required = false) String category,
      @RequestParam(name = "min_cost", required = false) BigDecimal minCost,
      @RequestParam(name = "max_cost", required = false) BigDecimal maxCost,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(required = false) String sort) {

    Pageable pageable = buildPageable(page, limit, sort);

    ToolSearchCriteria criteria =
        new ToolSearchCriteria(department, status, category, minCost, maxCost);

    return toolService.getTools(criteria, pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ToolDetailResponse> getTool(@PathVariable Integer id) {
    return ResponseEntity.ok(toolService.getToolById(id));
  }

  @PostMapping
  public ResponseEntity<ToolResponse> createTool(@Valid @RequestBody CreateToolRequest request) {
    ToolResponse tool = toolService.createTool(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(tool);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ToolResponse> updateTool(
      @PathVariable Integer id, @Valid @RequestBody UpdateToolRequest request) {
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
