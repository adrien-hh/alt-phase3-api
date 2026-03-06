package org.alt.altphase3api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.alt.altphase3api.domain.bo.Category;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.domain.bo.UsageLog;
import org.alt.altphase3api.domain.bo.User;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import org.alt.altphase3api.domain.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UsageLogRepositoryTest {

  @Autowired private UsageLogRepository usageLogRepository;

  @Autowired private ToolRepository toolRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private TestEntityManager entityManager;

  @Test
  @DisplayName("countByToolIdAndSessionDateAfter returns correct count")
  void countByToolIdAndSessionDateAfter_returnsCount() {
    Tool tool = createTool("Slack");
    User user = createUser("john.doe@test.com");

    saveUsageLog(tool, user, LocalDate.of(2025, 5, 10), 30);
    saveUsageLog(tool, user, LocalDate.of(2025, 5, 15), 60);
    saveUsageLog(tool, user, LocalDate.of(2025, 5, 1), 45);

    long count =
        usageLogRepository.countByToolIdAndSessionDateAfter(tool.getId(), LocalDate.of(2025, 5, 5));

    assertThat(count).isEqualTo(2);
  }

  @Test
  @DisplayName("findAverageUsageMinutes returns correct average")
  void findAverageUsageMinutes_returnsAverage() {
    Tool tool = createTool("Confluence");
    User user = createUser("jane.doe@test.com");

    saveUsageLog(tool, user, LocalDate.of(2025, 5, 10), 30);
    saveUsageLog(tool, user, LocalDate.of(2025, 5, 12), 60);
    saveUsageLog(tool, user, LocalDate.of(2025, 5, 15), 90);

    Double average =
        usageLogRepository.findAverageUsageMinutes(tool.getId(), LocalDate.of(2025, 5, 1));

    assertThat(average).isEqualTo(60.0);
  }

  @Test
  @DisplayName("findAverageUsageMinutes returns zero when no result")
  void findAverageUsageMinutes_returnsZeroWhenNoMatch() {
    Tool tool = createTool("Linear");

    Double average =
        usageLogRepository.findAverageUsageMinutes(tool.getId(), LocalDate.of(2025, 5, 1));

    assertThat(average).isEqualTo(0.0);
  }

  private Tool createTool(String name) {
    Category category = new Category();
    category.setName("Development");
    category = categoryRepository.save(category);

    Tool tool = new Tool();
    tool.setName(name);
    tool.setDescription("Test tool");
    tool.setVendor("Test vendor");
    tool.setWebsiteUrl("https://example.com");
    tool.setCategory(category);
    tool.setMonthlyCost(new BigDecimal("10.00"));
    tool.setOwnerDepartment(Department.Engineering);
    tool.setStatus(ToolStatus.active);
    tool.setActiveUsersCount(5);
    tool.setCreatedAt(Instant.now());
    tool.setUpdatedAt(Instant.now());

    return toolRepository.save(tool);
  }

  private User createUser(String email) {
    User user =
        User.builder()
            .name("John Doe")
            .email(email)
            .department(Department.Engineering)
            .status(UserStatus.active)
            .createdAt(Instant.now())
            .build();

    return entityManager.persist(user);
  }

  private void saveUsageLog(Tool tool, User user, LocalDate sessionDate, int usageMinutes) {
    UsageLog log = new UsageLog();
    log.setTool(tool);
    log.setUser(user);
    log.setSessionDate(sessionDate);
    log.setUsageMinutes(usageMinutes);
    log.setActionsCount(10);
    log.setCreatedAt(Instant.now());

    usageLogRepository.save(log);
  }
}
