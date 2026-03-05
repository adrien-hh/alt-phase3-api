package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.alt.altphase3api.domain.enums.AccessRequestStatus;

@Getter
@Setter
@Entity
@Table(name = "access_requests")
public class AccessRequest {
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

  @Column(name = "business_justification", nullable = false, columnDefinition = "TEXT")
  private String businessJustification;

  @Enumerated(EnumType.STRING)
  @Column(name="status")
  private AccessRequestStatus status = AccessRequestStatus.pending;

  @Column(name = "requested_at")
  private Instant requestedAt;

  @Column(name = "processed_at")
  private Instant processedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processed_by")
  private User processedBy;

  @Column(name = "processing_notes", columnDefinition = "TEXT")
  private String processingNotes;
}
