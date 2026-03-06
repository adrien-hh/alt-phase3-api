package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.alt.altphase3api.domain.enums.UserToolAccessStatus;

@Getter
@Setter
@Entity
@Table(name = "user_tool_access")
public class UserToolAccess {
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

  @Column(name = "granted_at")
  private Instant grantedAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "granted_by", nullable = false)
  private User grantedBy;

  @Column(name = "revoked_at")
  private Instant revokedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "revoked_by")
  private User revokedBy;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private UserToolAccessStatus status;
}
