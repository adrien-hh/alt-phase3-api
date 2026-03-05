package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;

@Getter
@Setter
@Entity
@Table(name = "tools")
public class Tool {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "vendor", length = 100)
  private String vendor;

  @Column(name = "website_url")
  private String websiteUrl;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(name = "monthly_cost", nullable = false, precision = 10, scale = 2)
  private BigDecimal monthlyCost;

  @Column(name = "active_users_count", nullable = false)
  private Integer activeUsersCount = 0;

  @Enumerated(EnumType.STRING)
  @Column(name = "owner_department")
  private Department ownerDepartment;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private ToolStatus status = ToolStatus.active;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;
}
