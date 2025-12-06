package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@SuperBuilder
public class CraftDTO extends AstralBodyDTO {
  private double engineIsp;
  private double engineThrustNewtons;
  private double dryMass;
  private double remainingDeltaV;

  @OneToOne(cascade = CascadeType.ALL)
  private KeplerOrbit initialOrbit;

  @OneToOne(cascade = CascadeType.ALL)
  private KeplerOrbit finalOrbit;
}
