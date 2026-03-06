package org.alt.altphase3api.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alt.altphase3api.domain.bo.Category;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.dto.*;
import org.alt.altphase3api.exception.ResourceNotFoundException;
import org.alt.altphase3api.repository.CategoryRepository;
import org.alt.altphase3api.repository.ToolRepository;
import org.alt.altphase3api.repository.UsageLogRepository;
import org.alt.altphase3api.repository.spec.ToolSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToolService {

  private final ToolRepository toolRepository;
  private final CategoryRepository categoryRepository;
  private final UsageLogRepository usageLogRepository;

  private final LocalDate USAGE_START_DATE = LocalDate.now().minusDays(30);

  public ToolListResponse getTools(ToolSearchCriteria criteria, Pageable pageable) {
    log.info(
        "Listing tools with criteria={}, page={}, size={}",
        criteria,
        pageable.getPageNumber(),
        pageable.getPageSize());
    Specification<Tool> spec = ToolSpecifications.withFilters(criteria);

    Page<Tool> page = toolRepository.findAll(spec, pageable);

    long total = toolRepository.count();
    long filtered = page.getTotalElements();

    List<ToolResponse> data = page.getContent().stream().map(ToolResponse::from).toList();

    Map<String, Object> filtersApplied = findFilters(criteria);

    return new ToolListResponse(data, total, filtered, filtersApplied);
  }

  private Map<String, Object> findFilters(ToolSearchCriteria criteria) {
    Map<String, Object> filtersApplied = new LinkedHashMap<>();

    if (criteria.department() != null) {
      filtersApplied.put("department", criteria.department());
    }
    if (criteria.status() != null) {
      filtersApplied.put("status", criteria.status());
    }
    if (criteria.category() != null && !criteria.category().isBlank()) {
      filtersApplied.put("category", criteria.category());
    }
    if (criteria.minCost() != null) {
      filtersApplied.put("min_cost", criteria.minCost());
    }
    if (criteria.maxCost() != null) {
      filtersApplied.put("max_cost", criteria.maxCost());
    }

    return filtersApplied;
  }

  public ToolDetailResponse getToolById(Integer id) {
    log.info("Fetching tool id={}", id);
    Tool tool =
        toolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tool", id));

    long totalSessions = usageLogRepository.countByToolIdAndSessionDateAfter(id, USAGE_START_DATE);

    Double avgMinutes = usageLogRepository.findAverageUsageMinutes(id, USAGE_START_DATE);

    int avgSessionMinutes = avgMinutes == null ? 0 : (int) Math.round(avgMinutes);

    return ToolDetailResponse.from(tool, totalSessions, avgSessionMinutes);
  }

  public ToolResponse createTool(CreateToolRequest request) {
    log.info("Creating tool name={}, vendor={}", request.name(), request.vendor());

    Category category =
        categoryRepository
            .findById(request.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));
    Tool tool = request.toTool(category);
    return ToolResponse.from(toolRepository.save(tool));
  }

  public ToolResponse updateTool(Integer id, UpdateToolRequest request) {
    log.info("Updating tool id={}", id);

    Tool tool =
        toolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tool", id));
    applyUpdates(tool, request);

    return ToolResponse.from(toolRepository.save(tool));
  }

  private void applyUpdates(Tool tool, UpdateToolRequest request) {
    if (request.name() != null) {
      tool.setName(request.name());
    }
    if (request.description() != null) {
      tool.setDescription(request.description());
    }
    if (request.vendor() != null) {
      tool.setVendor(request.vendor());
    }
    if (request.websiteUrl() != null) {
      tool.setWebsiteUrl(request.websiteUrl());
    }
    if (request.monthlyCost() != null) {
      tool.setMonthlyCost(request.monthlyCost());
    }
    if (request.ownerDepartment() != null) {
      tool.setOwnerDepartment(request.ownerDepartment());
    }
    if (request.status() != null) {
      tool.setStatus(request.status());
    }

    if (request.categoryId() != null) {
      Category category =
          categoryRepository
              .findById(request.categoryId())
              .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));
      tool.setCategory(category);
    }
  }
}
