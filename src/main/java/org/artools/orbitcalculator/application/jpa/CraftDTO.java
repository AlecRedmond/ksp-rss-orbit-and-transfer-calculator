package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.hibernate.annotations.UuidGenerator;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class CraftDTO {
  @Id @UuidGenerator private String id;
  private double bodyRadius;
  private double engineIsp;
  private double engineThrustNewtons;
  private double currentMass;
  private double dryMass;
  private double remainingDeltaV;

  @OneToOne(cascade = CascadeType.ALL)
  private KeplerOrbit orbit;

  @OneToOne(cascade = CascadeType.ALL)
  private CraftItineraryDTO craftItineraryDTO;
}
