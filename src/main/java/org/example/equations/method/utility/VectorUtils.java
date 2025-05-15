package org.example.equations.method.utility;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;
import org.example.equations.application.Orbit;

@AllArgsConstructor
@NoArgsConstructor
public class VectorUtils {
  private Body body = Body.EARTH;

  public Vector3D velocityVector(Orbit orbit, double trueAnomaly) {
    double verticalVelocity = tangentialVelocity(orbit, trueAnomaly);
    double tangentialVelocity = verticalVelocity(orbit, trueAnomaly);
    return new Vector3D(new double[] {verticalVelocity, tangentialVelocity, 0});
  }

  private double tangentialVelocity(Orbit orbit, double trueAnomaly) {
    double mu = body.getMu();
    double p = semiLatusRectum(orbit);
    double e = orbit.getDataFor(ECCENTRICITY);
    double f = trueAnomaly;
    return Math.sqrt(mu / p) * (1 + e * Math.cos(f));
  }

  private double verticalVelocity(Orbit orbit, double trueAnomaly) {
    double mu = body.getMu();
    double p = semiLatusRectum(orbit);
    double e = orbit.getDataFor(ECCENTRICITY);
    double f = trueAnomaly;
    return Math.sqrt(mu / p) * (e * Math.sin(f));
  }

  protected double semiLatusRectum(Orbit orbit) {
    return semiLatusRectum(orbit.getDataFor(SEMI_MAJOR_AXIS), orbit.getDataFor(ECCENTRICITY));
  }

  protected double semiLatusRectum(double semiMajorAxis, double eccentricity) {
    return semiMajorAxis * (1 - Math.pow(eccentricity, 2));
  }
}
