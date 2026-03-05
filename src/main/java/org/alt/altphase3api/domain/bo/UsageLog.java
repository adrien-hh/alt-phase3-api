package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usage_logs")
public class UsageLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tool_id", nullable = false)
  private Tool tool;

  @Column(name = "session_date", nullable = false)
  private LocalDate sessionDate;

  @Column(name = "usage_minutes")
  private Integer usageMinutes = 0;

  @Column(name = "actions_count")
  private Integer actionsCount = 0;

  @Column(name = "created_at")
  private Instant createdAt;
}
