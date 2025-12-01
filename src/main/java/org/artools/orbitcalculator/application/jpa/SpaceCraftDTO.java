package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.hibernate.annotations.UuidGenerator;

@Entity
@NoArgsConstructor
@Data
public class SpaceCraftDTO {
  @Id @UuidGenerator private String id;
  private double bodyRadius;
  private double engineIsp;
  private double currentMass;
  private double dryMass;
  private double remainingDeltaV;

  @OneToOne private KeplerOrbit currentOrbit;
}
