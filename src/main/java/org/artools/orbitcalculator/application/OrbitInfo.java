package org.artools.orbitcalculator.application;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.vector.entity.AstralPosition;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrbitInfo {
  @Id @UuidGenerator private String id;
  private String orbitFocus;
  private double apoapsisAlt;
  private double periapsisAlt;
  private double eccentricity;
  private double semiMajorAxis;
  private double rightAscension;
  private double inclination;
  private double argumentPE;
  private double trueAnomaly;
  private double eccentricAnomaly;
  private double meanAnomaly;
  private double altitude;
  private double orbitalVelocity;

  @OneToOne(optional = false)
  private AstralPosition astralPosition;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp timestamp;
}
