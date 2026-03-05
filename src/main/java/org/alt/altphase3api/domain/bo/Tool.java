package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(
    name = "tools",
    schema = "internal_tools",
    indexes = {
      @Index(name = "idx_tools_category", columnList = "category_id"),
      @Index(name = "idx_tools_cost_desc", columnList = "monthly_cost"),
      @Index(name = "idx_tools_active_users", columnList = "active_users_count"),
      @Index(name = "idx_tools_department", columnList = "owner_department"),
      @Index(name = "idx_tools_status", columnList = "status")
    })
public class Tool {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 100)
  @NotNull
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Lob
  @Column(name = "description")
  private String description;

  @Size(max = 100)
  @Column(name = "vendor", length = 100)
  private String vendor;

  @Size(max = 255)
  @Column(name = "website_url")
  private String websiteUrl;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @NotNull
  @Column(name = "monthly_cost", nullable = false, precision = 10, scale = 2)
  private BigDecimal monthlyCost;

  @NotNull
  @ColumnDefault("0")
  @Column(name = "active_users_count", nullable = false)
  private Integer activeUsersCount;

  @NotNull
  @Lob
  @Column(name = "owner_department", nullable = false)
  private String ownerDepartment;

  @ColumnDefault("'active'")
  @Lob
  @Column(name = "status")
  private String status;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt;
}
