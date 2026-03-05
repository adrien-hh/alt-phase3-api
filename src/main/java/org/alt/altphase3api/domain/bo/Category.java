package org.alt.altphase3api.domain.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(
    name = "categories",
    schema = "internal_tools",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "name",
          columnNames = {"name"})
    })
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 50)
  @NotNull
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Lob
  @Column(name = "description")
  private String description;

  @Size(max = 7)
  @ColumnDefault("'#6366f1'")
  @Column(name = "color_hex", length = 7)
  private String colorHex;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;
}
