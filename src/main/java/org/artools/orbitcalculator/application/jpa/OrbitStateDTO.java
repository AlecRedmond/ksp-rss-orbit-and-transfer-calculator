package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrbitStateDTO {
  @Id @UuidGenerator private String id;
  private BodyType centralBody;
  private double apoapsisAlt;
  private double periapsisAlt;
  private double eccentricity;
  private double semiMajorAxis;
  private double longitudeAscendingNode;
  private double inclination;
  private double argumentPE;
  private double trueAnomaly;
  private double eccentricAnomaly;
  private double meanAnomaly;
  private double altitude;
  private double orbitalVelocity;

  @OneToOne() private AstralPositionDTO astralPositionDTO;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp timestamp;
}
