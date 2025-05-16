package org.example.equations.method.vector;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;
import static org.example.equations.application.vector.ReferenceFrame.CRAFT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.CraftVectors;
import org.example.equations.application.vector.ReferenceFrame;

@Getter
@NoArgsConstructor
public class CraftVectorController {
  private CraftVectors vectors;

  public CraftVectorController buildVectors(Orbit orbit, double trueAnomaly) {
    var position = radiusVector(orbit, trueAnomaly);
    var velocity = velocityVector(orbit, trueAnomaly);
    var momentum = position.crossProduct(velocity);
    vectors =
        CraftVectors.builder()
            .position(position)
            .velocity(velocity)
            .momentum(momentum)
            .orbit(orbit)
            .trueAnomaly(trueAnomaly)
            .frame(CRAFT)
            .build();
    return this;
  }

  public CraftVectorController changeFrame(
      ReferenceFrame newFrame) {
    AngleTransform transform = new AngleTransform();
    vectors = transform.rotateCraftVectors(vectors, newFrame);
    return this;
  }

  protected Vector3D velocityVector(Orbit orbit, double trueAnomaly) {
    var verticalVelocity = verticalVelocity(orbit, trueAnomaly);
    var tangentialVelocity = tangentialVelocity(orbit, trueAnomaly);
    return new Vector3D(new double[] {verticalVelocity, tangentialVelocity, 0});
  }

  protected Vector3D radiusVector(Orbit orbit, double trueAnomaly) {
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
