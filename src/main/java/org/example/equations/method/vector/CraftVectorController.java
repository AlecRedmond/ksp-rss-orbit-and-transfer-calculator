package org.example.equations.method.vector;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.CraftVectors;
import org.example.equations.application.vector.CraftVectorsMap;

@Getter
@NoArgsConstructor
public class CraftVectorController {
  private CraftVectorsMap craftVectorsMap = new CraftVectorsMap();

  public CraftVectorController buildVectors(Orbit orbit, double trueAnomaly) {
    Body body = orbit.getBody();
    var velocity = velocityVector(orbit, trueAnomaly);
    var radius = radiusVector(orbit, trueAnomaly);
    var rotation =
        new AngleTransform()
            .inertialFromCraft(
                orbit.getDataFor(RIGHT_ASCENSION),
                orbit.getDataFor(INCLINATION),
                orbit.getDataFor(ARGUMENT_PE),
                trueAnomaly);
    craftVectorsMap.putData(new CraftVectors(body, velocity, radius, rotation));
    return this;
  }


  public Optional<CraftVectors> getSOIVectors() {
    var body = getSphereOfInfluence();
    return body.isPresent() ? craftVectorsMap.getCraftVectors(body.get()) : Optional.empty();
  }

  public Optional<Body> getSphereOfInfluence() {
    return craftVectorsMap.getMap().entrySet().stream()
        .max(Comparator.comparing(this::accelerationMagnitude))
        .map(Map.Entry::getKey);
  }

  private double accelerationMagnitude(Map.Entry<Body, CraftVectors> entry) {
    return entry.getValue().getAcceleration().getNorm();
  }

  private Vector3D velocityVector(Orbit orbit, double trueAnomaly) {
    var verticalVelocity = verticalVelocity(orbit, trueAnomaly);
    var tangentialVelocity = tangentialVelocity(orbit, trueAnomaly);
    return new Vector3D(new double[] {verticalVelocity, tangentialVelocity, 0});
  }

  private Vector3D radiusVector(Orbit orbit, double trueAnomaly) {
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
