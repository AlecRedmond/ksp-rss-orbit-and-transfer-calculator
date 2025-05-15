package org.example.equations.method.referenceframes;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;

@NoArgsConstructor
public class VectorBuilder {

  public Vector3D velocityVector(Orbit orbit, double trueAnomaly) {
    var verticalVelocity = verticalVelocity(orbit, trueAnomaly);
    var tangentialVelocity = tangentialVelocity(orbit, trueAnomaly);
    return new Vector3D(new double[] {verticalVelocity, tangentialVelocity, 0});
  }

  public Vector3D radiusVector(Orbit orbit, double trueAnomaly) {
    var a = orbit.getDataFor(SEMI_MAJOR_AXIS);
    var e = orbit.getDataFor(ECCENTRICITY);
    var f = trueAnomaly;
    var radius = a * (1 - Math.pow(e, 2)) / (1 + e * Math.cos(f));
    return new Vector3D(new double[] {radius, 0, 0});
  }

  private double tangentialVelocity(Orbit orbit, double trueAnomaly) {
    var mu = orbit.getBody().getMu();
    var p = semiLatusRectum(orbit);
    var e = orbit.getDataFor(ECCENTRICITY);
    var f = trueAnomaly;
    return Math.sqrt(mu / p) * (1 + e * Math.cos(f));
  }

  private double verticalVelocity(Orbit orbit, double trueAnomaly) {
    var mu = orbit.getBody().getMu();
    var p = semiLatusRectum(orbit);
    var e = orbit.getDataFor(ECCENTRICITY);
    var f = trueAnomaly;
    return Math.sqrt(mu / p) * (e * Math.sin(f));
  }

  private double semiLatusRectum(Orbit orbit) {
    return semiLatusRectum(orbit.getDataFor(SEMI_MAJOR_AXIS), orbit.getDataFor(ECCENTRICITY));
  }

  private double semiLatusRectum(double semiMajorAxis, double eccentricity) {
    return semiMajorAxis * (1 - Math.pow(eccentricity, 2));
  }
}
