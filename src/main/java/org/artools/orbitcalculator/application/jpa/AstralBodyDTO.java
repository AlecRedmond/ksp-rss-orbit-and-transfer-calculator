package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AstralBodyDTO {
  @Id @UuidGenerator private String id;

  private double bodyRadius;
  private double mass;
  private double mu;

  @OneToMany(cascade = CascadeType.ALL)
  private List<AstralStateDTO> snapshots;
}
