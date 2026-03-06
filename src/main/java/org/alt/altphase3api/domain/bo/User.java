package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import lombok.*;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.Role;
import org.alt.altphase3api.domain.enums.UserStatus;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "email", nullable = false, length = 150, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "department", nullable = false)
  private Department department;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  @Builder.Default
  private Role role = Role.employee;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  @Builder.Default
  private UserStatus status = UserStatus.active;

  @Column(name = "hire_date")
  private LocalDate hireDate;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;
}
