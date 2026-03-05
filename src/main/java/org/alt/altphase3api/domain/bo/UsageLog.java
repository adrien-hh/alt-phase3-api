package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    name = "usage_logs",
    schema = "internal_tools",
    indexes = {
      @Index(name = "idx_usage_user_date", columnList = "user_id, session_date"),
      @Index(name = "tool_id", columnList = "tool_id"),
      @Index(name = "idx_usage_date_tool", columnList = "session_date, tool_id")
    })
public class UsageLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "tool_id", nullable = false)
  private Tool tool;

  @NotNull
  @Column(name = "session_date", nullable = false)
  private LocalDate sessionDate;

  @ColumnDefault("0")
  @Column(name = "usage_minutes")
  private Integer usageMinutes;

  @ColumnDefault("0")
  @Column(name = "actions_count")
  private Integer actionsCount;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;
}
