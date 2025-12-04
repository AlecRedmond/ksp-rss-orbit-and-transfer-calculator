package org.artools.orbitcalculator.application.vector;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.planets.Planet;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrbitalState extends MotionState {
  private Planet centralBody;
  private Vector3D momentum;
  private Vector3D eccentricity;
  private double semiMajorAxis;
  private double longitudeAscendingNode;
  private double inclination;
  private double argumentPE;
  private double trueAnomaly;
  private double eccentricAnomaly;
  private double meanAnomaly;
  private double apoapsisAltitude;
  private double periapsisAltitude;
}
