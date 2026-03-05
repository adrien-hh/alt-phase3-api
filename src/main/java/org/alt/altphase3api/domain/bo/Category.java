package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 50)
  @NotNull
  @Column(name = "name", nullable = false, length = 50, unique = true)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Size(max = 7)
  @Column(name = "color_hex", length = 7)
  private String colorHex = "#6366f1";

  @Column(name = "created_at")
  private Instant createdAt;
}
