package org.alt.altphase3api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.alt.altphase3api.domain.bo.Category;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.dto.*;
import org.alt.altphase3api.exception.ResourceNotFoundException;
import org.alt.altphase3api.repository.CategoryRepository;
import org.alt.altphase3api.repository.ToolRepository;
import org.alt.altphase3api.repository.UsageLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ToolServiceTest {

  @Mock private ToolRepository toolRepository;

  @Mock private CategoryRepository categoryRepository;

  @Mock private UsageLogRepository usageLogRepository;

  @InjectMocks private ToolService toolService;

  private Tool tool;
  private Category category;

  @BeforeEach
  void setup() {
    category = new Category();
    category.setId(1);
    category.setName("Development");

    tool = new Tool();
    tool.setId(1);
    tool.setName("Slack");
    tool.setCategory(category);
    tool.setMonthlyCost(new BigDecimal("8.00"));
    tool.setActiveUsersCount(25);
  }

  @Test
  void getTools_returnsPaginatedResult() {

    Page<Tool> page = new PageImpl<>(List.of(tool), PageRequest.of(0, 10), 20);

    when(toolRepository.findAll(ArgumentMatchers.<Specification<Tool>>any(), any(Pageable.class)))
        .thenReturn(page);
    when(toolRepository.count()).thenReturn(20L);

    ToolSearchCriteria criteria = new ToolSearchCriteria(null, null, null, null, null);

    Pageable pageable = PageRequest.of(0, 10);

    ToolListResponse response = toolService.getTools(criteria, pageable);

    assertThat(response.total()).isEqualTo(20);
    assertThat(response.filtered()).isEqualTo(20);
    assertThat(response.data()).hasSize(1);
  }

  @Test
  void getTools_returnsEmptyList() {

    Page<Tool> emptyPage = Page.empty();

    when(toolRepository.findAll(ArgumentMatchers.<Specification<Tool>>any(), any(Pageable.class)))
        .thenReturn(emptyPage);

    when(toolRepository.count()).thenReturn(20L);

    ToolSearchCriteria criteria = new ToolSearchCriteria(null, null, null, null, null);

    Pageable pageable = PageRequest.of(0, 10);

    ToolListResponse response = toolService.getTools(criteria, pageable);

    assertThat(response.data()).isEmpty();
    assertThat(response.filtered()).isEqualTo(0);
  }

  @Test
  void getToolById_returnsDetail() {

    when(toolRepository.findById(tool.getId())).thenReturn(Optional.of(tool));
    when(usageLogRepository.countByToolIdAndSessionDateAfter(any(), any())).thenReturn(10L);
    when(usageLogRepository.findAverageUsageMinutes(any(), any())).thenReturn(20.0);

    ToolDetailResponse response = toolService.getToolById(tool.getId());

    assertThat(response.id()).isEqualTo(tool.getId());
    assertThat(response.totalMonthlyCost())
        .isEqualByComparingTo(
            tool.getMonthlyCost().multiply(BigDecimal.valueOf(tool.getActiveUsersCount())));
  }

  @Test
  void getToolById_throwsWhenNotFound() {

    when(toolRepository.findById(99)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> toolService.getToolById(99));
  }

  @Test
  void createTool_createsTool() {

    CreateToolRequest request = newCreateRequest();

    when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
    when(toolRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    ToolResponse response = toolService.createTool(request);

    assertThat(response.name()).isEqualTo(tool.getName());
    verify(toolRepository).save(any());
  }

  @Test
  void createTool_throwsWhenCategoryMissing() {

    CreateToolRequest request = newCreateRequest();

    when(categoryRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> toolService.createTool(request));
  }

  @Test
  void updateTool_updatesOnlyProvidedFields() {

    UpdateToolRequest request = newUpdateRequest();

    when(toolRepository.findById(1)).thenReturn(Optional.of(tool));
    when(toolRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    ToolResponse response = toolService.updateTool(1, request);

    // updated fields
    assertThat(response.description()).isEqualTo(request.description());
    assertThat(response.monthlyCost()).isEqualByComparingTo(request.monthlyCost());

    // unchanged fields
    assertThat(response.name()).isEqualTo(tool.getName());
    assertThat(response.status()).isEqualTo(tool.getStatus());
  }

  @Test
  void updateTool_withEmptyRequest_doesNotChangeAnything() {

    UpdateToolRequest request = UpdateToolRequest.builder().build();

    when(toolRepository.findById(1)).thenReturn(Optional.of(tool));
    when(toolRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    ToolResponse response = toolService.updateTool(1, request);

    assertThat(response.name()).isEqualTo(tool.getName());
    assertThat(response.description()).isEqualTo(tool.getDescription());
    assertThat(response.monthlyCost()).isEqualByComparingTo(tool.getMonthlyCost());
    assertThat(response.status()).isEqualTo(tool.getStatus());

    verify(toolRepository).save(tool);
  }

  @Test
  void updateTool_callsSaveOnce() {

    UpdateToolRequest request = newUpdateRequest();

    when(toolRepository.findById(1)).thenReturn(Optional.of(tool));
    when(toolRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    toolService.updateTool(1, request);

    verify(toolRepository, times(1)).save(any());
  }

  private CreateToolRequest newCreateRequest() {
    return CreateToolRequest.builder()
        .name("Slack")
        .description("Team messaging platform")
        .vendor("Slack Technologies")
        .websiteUrl("https://slack.com")
        .categoryId(1)
        .monthlyCost(new BigDecimal("8.00"))
        .ownerDepartment(Department.Engineering)
        .build();
  }

  private UpdateToolRequest newUpdateRequest() {
    return UpdateToolRequest.builder()
        .description("Updated description")
        .monthlyCost(new BigDecimal("7.00"))
        .build();
  }
}
