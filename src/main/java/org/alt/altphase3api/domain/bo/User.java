package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(
    name = "users",
    schema = "internal_tools",
    indexes = {
      @Index(name = "idx_users_department", columnList = "department"),
      @Index(name = "idx_users_status", columnList = "status")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "email",
          columnNames = {"email"})
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 100)
  @NotNull
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Size(max = 150)
  @NotNull
  @Column(name = "email", nullable = false, length = 150)
  private String email;

  @NotNull
  @Lob
  @Column(name = "department", nullable = false)
  private String department;

  @ColumnDefault("'employee'")
  @Lob
  @Column(name = "role")
  private String role;

  @ColumnDefault("'active'")
  @Lob
  @Column(name = "status")
  private String status;

  @Column(name = "hire_date")
  private LocalDate hireDate;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt;
}
