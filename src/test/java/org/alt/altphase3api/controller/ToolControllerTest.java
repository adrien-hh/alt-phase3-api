package org.alt.altphase3api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import org.alt.altphase3api.dto.ToolDetailResponse;
import org.alt.altphase3api.dto.ToolListResponse;
import org.alt.altphase3api.dto.ToolResponse;
import org.alt.altphase3api.dto.UsageMetrics;
import org.alt.altphase3api.exception.GlobalExceptionHandler;
import org.alt.altphase3api.exception.ResourceNotFoundException;
import org.alt.altphase3api.service.ToolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ToolController.class)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class ToolControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ToolService toolService;

  @Test
  @DisplayName("GET /api/tools returns 200 with paginated list")
  void getTools_returns200AndList() throws Exception {
    ToolResponse tool = buildToolResponse();

    ToolListResponse response =
        new ToolListResponse(List.of(tool), 20, 1, Map.of("department", "Engineering"));

    when(toolService.getTools(any(), any(Pageable.class))).thenReturn(response);

    mockMvc
        .perform(
            get("/api/tools")
                .param("department", "Engineering")
                .param("page", "0")
                .param("limit", "10")
                .param("sort", "name,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").value(1))
        .andExpect(jsonPath("$.data[0].name").value("Slack"))
        .andExpect(jsonPath("$.data[0].website_url").value("https://slack.com"))
        .andExpect(jsonPath("$.data[0].monthly_cost").value(8.00))
        .andExpect(jsonPath("$.data[0].owner_department").value("Engineering"))
        .andExpect(jsonPath("$.data[0].active_users_count").value(25))
        .andExpect(jsonPath("$.total").value(20))
        .andExpect(jsonPath("$.filtered").value(1))
        .andExpect(jsonPath("$.filters_applied.department").value("Engineering"));

    verify(toolService).getTools(any(), any(Pageable.class));
  }

  @Test
  @DisplayName("GET /api/tools returns empty list when no result")
  void getTools_returnsEmptyListWhenNoResult() throws Exception {
    ToolListResponse response = new ToolListResponse(List.of(), 20, 0, Map.of("min_cost", 9999));

    when(toolService.getTools(any(), any(Pageable.class))).thenReturn(response);

    mockMvc
        .perform(get("/api/tools").param("min_cost", "9999"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data").isEmpty())
        .andExpect(jsonPath("$.total").value(20))
        .andExpect(jsonPath("$.filtered").value(0))
        .andExpect(jsonPath("$.filters_applied.min_cost").value(9999));
  }

  @Test
  @DisplayName("GET /api/tools/{id} returns 200 with details")
  void getToolById_returns200AndDetail() throws Exception {
    ToolDetailResponse response = buildToolDetailResponse();

    when(toolService.getToolById(5)).thenReturn(response);

    mockMvc
        .perform(get("/api/tools/5"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(5))
        .andExpect(jsonPath("$.name").value("Confluence"))
        .andExpect(jsonPath("$.category").value("Development"))
        .andExpect(jsonPath("$.monthly_cost").value(5.50))
        .andExpect(jsonPath("$.active_users_count").value(9))
        .andExpect(jsonPath("$.total_monthly_cost").value(49.50))
        .andExpect(jsonPath("$.usage_metrics.last_30_days.total_sessions").value(127))
        .andExpect(jsonPath("$.usage_metrics.last_30_days.avg_session_minutes").value(45));
  }

  @Test
  @DisplayName("GET /api/tools/{id} returns 404 when tool does not exist")
  void getToolById_returns404WhenNotFound() throws Exception {
    when(toolService.getToolById(999)).thenThrow(new ResourceNotFoundException("Tool", 999));

    mockMvc
        .perform(get("/api/tools/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Tool not found"))
        .andExpect(jsonPath("$.message").value("Tool with ID 999 does not exist"));
  }

  @Test
  @DisplayName("POST /api/tools returns 201 when payload is valid")
  void createTool_returns201WhenValid() throws Exception {
    ToolResponse response =
        new ToolResponse(
            21,
            "Linear",
            "Issue tracking and project management",
            "Linear",
            "https://linear.app",
            "Development",
            new BigDecimal("8.00"),
            Department.Engineering,
            ToolStatus.active,
            0,
            Instant.parse("2025-08-20T14:30:00Z"),
            Instant.parse("2025-08-20T14:30:00Z"));

    when(toolService.createTool(any())).thenReturn(response);

    String body =
        """
                  {
                  "name": "Linear",
                  "description": "Issue tracking and project management",
                  "vendor": "Linear",
                  "website_url": "https://linear.app",
                  "category_id": 2,
                  "monthly_cost": 8.00,
                  "owner_department": "Engineering"
                }
                """;

    mockMvc
        .perform(post("/api/tools").contentType(APPLICATION_JSON).content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(21))
        .andExpect(jsonPath("$.name").value("Linear"))
        .andExpect(jsonPath("$.category").value("Development"))
        .andExpect(jsonPath("$.status").value("active"))
        .andExpect(jsonPath("$.active_users_count").value(0));

    verify(toolService).createTool(any());
  }

  @Test
  @DisplayName("POST /api/tools returns 400 when payload is invalid")
  void createTool_returns400WhenInvalid() throws Exception {
    String body =
        """
                {
                  "name": "",
                  "vendor": "Vendor",
                  "website_url": "not-a-url",
                  "category_id": 2,
                  "monthly_cost": -5,
                  "owner_department": "Engineering"
                }
                """;

    mockMvc
        .perform(post("/api/tools").contentType(APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Validation failed"))
        .andExpect(jsonPath("$.details.name").exists())
        .andExpect(jsonPath("$.details.website_url").exists())
        .andExpect(jsonPath("$.details.monthly_cost").exists());
  }

  @Test
  @DisplayName("PUT /api/tools/{id} returns 200 when payload is valid")
  void updateTool_returns200WhenValid() throws Exception {
    ToolResponse response =
        new ToolResponse(
            5,
            "Confluence",
            "Updated description after renewal",
            "Atlassian",
            "https://confluence.atlassian.com",
            "Development",
            new BigDecimal("7.00"),
            Department.Engineering,
            ToolStatus.deprecated,
            9,
            Instant.parse("2025-05-01T09:00:00Z"),
            Instant.parse("2025-08-20T15:45:00Z"));

    when(toolService.updateTool(eq(5), any())).thenReturn(response);

    String body =
        """
                {
                  "monthly_cost": 7.00,
                  "status": "deprecated",
                  "description": "Updated description after renewal"
                }
                """;

    mockMvc
        .perform(put("/api/tools/5").contentType(APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(5))
        .andExpect(jsonPath("$.description").value("Updated description after renewal"))
        .andExpect(jsonPath("$.monthly_cost").value(7.00))
        .andExpect(jsonPath("$.status").value("deprecated"));

    verify(toolService).updateTool(eq(5), any());
  }

  @Test
  @DisplayName("PUT /api/tools/{id} returns 404 when tool does not exist")
  void updateTool_returns404WhenNotFound() throws Exception {
    when(toolService.updateTool(eq(999), any()))
        .thenThrow(new ResourceNotFoundException("Tool", 999));

    String body =
        """
                {
                  "monthly_cost": 7.00
                }
                """;

    mockMvc
        .perform(put("/api/tools/999").contentType(APPLICATION_JSON).content(body))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Tool not found"))
        .andExpect(jsonPath("$.message").value("Tool with ID 999 does not exist"));
  }

  private ToolResponse buildToolResponse() {
    return new ToolResponse(
        1,
        "Slack",
        "Team messaging platform",
        "Slack Technologies",
        "https://slack.com",
        "Communication",
        new BigDecimal("8.00"),
        Department.Engineering,
        ToolStatus.active,
        25,
        Instant.parse("2025-05-01T09:00:00Z"),
        Instant.parse("2025-05-01T09:00:00Z"));
  }

  private ToolDetailResponse buildToolDetailResponse() {
    return new ToolDetailResponse(
        5,
        "Confluence",
        "Team collaboration and documentation",
        "Atlassian",
        "https://confluence.atlassian.com",
        "Development",
        new BigDecimal("5.50"),
        Department.Engineering,
        ToolStatus.active,
        9,
        new BigDecimal("49.50"),
        Instant.parse("2025-05-01T09:00:00Z"),
        Instant.parse("2025-05-01T09:00:00Z"),
        new UsageMetrics(new UsageMetrics.Last30Days(127, 45)));
  }
}
