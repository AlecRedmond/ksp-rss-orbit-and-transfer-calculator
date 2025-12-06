package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PlanetDTO extends AstralBodyDTO {
  @Enumerated(EnumType.STRING)
  private BodyType bodyType;
}
