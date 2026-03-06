package org.alt.altphase3api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.alt.altphase3api.domain.bo.Category;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.dto.CreateToolRequest;
import org.alt.altphase3api.dto.ToolResponse;
import org.alt.altphase3api.dto.UpdateToolRequest;
import org.alt.altphase3api.exception.ResourceNotFoundException;
import org.alt.altphase3api.repository.CategoryRepository;
import org.alt.altphase3api.repository.ToolRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToolService {

  private final ToolRepository toolRepository;
  private final CategoryRepository categoryRepository;

  public List<Tool> getAllTools() {
    return toolRepository.findAll();
  }

  public Tool getToolById(Integer id) {
    return toolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tool", id));
  }

  public ToolResponse createTool(CreateToolRequest request) {
    Category category =
        categoryRepository
            .findById(request.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));
    Tool tool = request.toTool(category);
    return ToolResponse.from(toolRepository.save(tool));
  }

  public ToolResponse updateTool(Integer id, UpdateToolRequest request) {
    Tool tool = toolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tool", id));
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
