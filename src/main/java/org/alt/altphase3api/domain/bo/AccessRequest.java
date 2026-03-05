package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.alt.altphase3api.domain.enums.AccessRequestStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(
    name = "access_requests",
    schema = "internal_tools",
    indexes = {
      @Index(name = "idx_requests_user", columnList = "user_id"),
      @Index(name = "tool_id", columnList = "tool_id"),
      @Index(name = "idx_requests_status", columnList = "status"),
      @Index(name = "idx_requests_date", columnList = "requested_at"),
      @Index(name = "processed_by", columnList = "processed_by")
    })
public class AccessRequest {
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
  @Column(name = "business_justification", nullable = false)
  private String businessJustification;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private AccessRequestStatus status = AccessRequestStatus.PENDING;

  @Column(name = "requested_at")
  private Instant requestedAt = Instant.now();

  @Column(name = "processed_at")
  private Instant processedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "processed_by")
  private User processedBy;

  @Column(name = "processing_notes")
  private String processingNotes;
}
