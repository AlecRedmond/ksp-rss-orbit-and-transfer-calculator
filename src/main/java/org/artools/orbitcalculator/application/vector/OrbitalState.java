package org.artools.orbitcalculator.application.vector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Body;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrbitalState extends MotionState {
  private Body centralBody;
  private Vector3D momentum;
  private Vector3D eccentricity;
  private double semiMajorAxis;
  private double rightAscension;
  private double inclination;
  private double argumentPE;
  private double trueAnomaly;
  private double eccentricAnomaly;
  private double meanAnomaly;
}
