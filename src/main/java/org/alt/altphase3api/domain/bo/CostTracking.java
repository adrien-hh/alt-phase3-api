package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(
    name = "cost_tracking",
    schema = "internal_tools",
    indexes = {@Index(name = "idx_cost_month_tool", columnList = "month_year, tool_id")},
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_tool_month",
          columnNames = {"tool_id", "month_year"})
    })
public class CostTracking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "tool_id", nullable = false)
  private Tool tool;

  @NotNull
  @Column(name = "month_year", nullable = false)
  private LocalDate monthYear;

  @NotNull
  @Column(name = "total_monthly_cost", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalMonthlyCost;

  @NotNull
  @ColumnDefault("0")
  @Column(name = "active_users_count", nullable = false)
  private Integer activeUsersCount;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;
}
