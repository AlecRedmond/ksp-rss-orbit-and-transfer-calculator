package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AstralPositionDTO {
  @Id @UuidGenerator private String id;

  @Enumerated(EnumType.STRING)
  private BodyType bodyType;

  private Double radius;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp timestamp;

  @Embedded private Vector3DTO position;

  @Embedded private Vector3DTO velocity;

  @OneToOne(cascade = CascadeType.ALL)
  private KeplerOrbit orbit;
}
