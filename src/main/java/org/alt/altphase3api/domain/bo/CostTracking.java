package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cost_tracking")
public class CostTracking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tool_id", nullable = false)
  private Tool tool;

  @Column(name = "month_year", nullable = false)
  private LocalDate monthYear;

  @Column(name = "total_monthly_cost", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalMonthlyCost;

  @Column(name = "active_users_count", nullable = false)
  private Integer activeUsersCount = 0;

  @Column(name = "created_at")
  private Instant createdAt;
}
