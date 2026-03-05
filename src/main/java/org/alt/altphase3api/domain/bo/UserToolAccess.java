package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(
    name = "user_tool_access",
    schema = "internal_tools",
    indexes = {
      @Index(name = "idx_access_user", columnList = "user_id"),
      @Index(name = "idx_access_tool", columnList = "tool_id"),
      @Index(name = "idx_access_granted_date", columnList = "granted_at"),
      @Index(name = "granted_by", columnList = "granted_by"),
      @Index(name = "revoked_by", columnList = "revoked_by"),
      @Index(name = "idx_access_status", columnList = "status")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_user_tool_active",
          columnNames = {"user_id", "tool_id", "status"})
    })
public class UserToolAccess {
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

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "granted_at")
  private Instant grantedAt;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "granted_by", nullable = false)
  private User grantedBy;

  @Column(name = "revoked_at")
  private Instant revokedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "revoked_by")
  private User revokedBy;

  @ColumnDefault("'active'")
  @Lob
  @Column(name = "status")
  private String status;
}
