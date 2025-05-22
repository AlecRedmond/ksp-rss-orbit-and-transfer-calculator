package org.example.equations.method.vector;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.MotionVectors;
import org.example.equations.application.vector.MotionVectorsMap;

@Getter
@NoArgsConstructor
public class MotionVectorBuilder {
  private final MotionVectorsMap motionVectorsMap = new MotionVectorsMap();

  public MotionVectorBuilder buildVectors(Orbit orbit, double trueAnomaly, Instant epoch) {
    Body body = orbit.getBody();
    var velocityAnomaly = velocityVector(orbit, trueAnomaly);
    var radiusAnomaly = getRadius(orbit, trueAnomaly);
    var transform =
        new AngleTransform()
            .setAnomalyAngle(trueAnomaly)
            .setOrbitAngles(orbit)
            .setVelocityAngle(velocityAnomaly, radiusAnomaly);
    var toMotion = transform.getToMotionInitializer();
    var bodyDistanceMotion = toMotion.applyTo(radiusAnomaly).negate();
    var velocityMotion = toMotion.applyTo(velocityAnomaly);
    var motionToInertial = transform.getInertialFromMotion();
    motionVectorsMap.putData(
        new MotionVectors(body, velocityMotion, bodyDistanceMotion, motionToInertial, epoch));
    return this;
  }

  public Optional<MotionVectors> getSOIVectors() {
    var body = getSphereOfInfluence();
    return body.isPresent() ? motionVectorsMap.getMotionVectors(body.get()) : Optional.empty();
  }

  public Optional<Body> getSphereOfInfluence() {
    return motionVectorsMap.getMap().entrySet().stream()
        .max(Comparator.comparing(this::accelerationMagnitude))
        .map(Map.Entry::getKey);
  }

  private double accelerationMagnitude(Map.Entry<Body, MotionVectors> entry) {
    return entry.getValue().getAcceleration().getNorm();
  }

  private Vector3D velocityVector(Orbit orbit, double trueAnomaly) {
    var verticalVelocity = verticalVelocity(orbit, trueAnomaly);
    var tangentialVelocity = tangentialVelocity(orbit, trueAnomaly);
    return new Vector3D(new double[] {verticalVelocity, tangentialVelocity, 0});
  }

  private Vector3D getRadius(Orbit orbit, double trueAnomaly) {
    var semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    var eccentricity = orbit.getDataFor(ECCENTRICITY);
    var radius =
        semiMajorAxis
            * (1 - Math.pow(eccentricity, 2))
            / (1 + eccentricity * Math.cos(trueAnomaly));
    return new Vector3D(new double[] {radius, 0, 0});
  }

  private double tangentialVelocity(Orbit orbit, double trueAnomaly) {
    var mu = orbit.getBody().getMu();
    var semiLatusRectum = semiLatusRectum(orbit);
    var eccentricity = orbit.getDataFor(ECCENTRICITY);
    return Math.sqrt(mu / semiLatusRectum) * (1 + eccentricity * Math.cos(trueAnomaly));
  }

  private double verticalVelocity(Orbit orbit, double trueAnomaly) {
    var mu = orbit.getBody().getMu();
    var semiLatusRectum = semiLatusRectum(orbit);
    var eccentricity = orbit.getDataFor(ECCENTRICITY);
    return Math.sqrt(mu / semiLatusRectum) * (eccentricity * Math.sin(trueAnomaly));
  }

  private double semiLatusRectum(Orbit orbit) {
    return semiLatusRectum(orbit.getDataFor(SEMI_MAJOR_AXIS), orbit.getDataFor(ECCENTRICITY));
  }

  private double semiLatusRectum(double semiMajorAxis, double eccentricity) {
    return semiMajorAxis * (1 - Math.pow(eccentricity, 2));
  }
}
