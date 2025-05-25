package org.example.equations.method.vector;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;
import static org.example.equations.application.vector.MotionVectors.*;
import static org.example.equations.application.vector.MotionVectors.Frame.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.MotionVectors;
import org.example.equations.application.vector.MotionVectorsMap;
import org.example.equations.application.vector.OrbitalVectors;
import org.example.equations.method.OrbitBuilder;

@Getter
@NoArgsConstructor
public class MotionVectorBuilder {
  private final MotionVectorsMap motionVectorsMap = new MotionVectorsMap();

  private static FrameTransform getTransform(
      Orbit orbit, double trueAnomaly, Vector3D velocityAnomaly, Vector3D radiusAnomaly) {
    return new FrameTransform()
        .setAnomalyAngle(trueAnomaly)
        .setOrbitAngles(orbit)
        .setVelocityAngle(velocityAnomaly, radiusAnomaly);
  }

  public MotionVectorBuilder buildVectors(
      Orbit orbit, double trueAnomaly, Instant epoch, Frame frame) {
    Vector3D anomalyFrameVelocity = anomalyVelocityVector(orbit, trueAnomaly);
    Vector3D anomalyFrameRadius = anomalyRadiusVector(orbit, trueAnomaly);

    FrameTransform transform =
        getTransform(orbit, trueAnomaly, anomalyFrameVelocity, anomalyFrameRadius);
    Rotation rotationToInertialFromVelocity = transform.getRotationToInertialFromVelocity();
    Rotation rotationToVelocityFromAnomaly = transform.getRotationToVelocityFromAnomaly();

    Vector3D radius = rotationToVelocityFromAnomaly.applyTo(anomalyFrameRadius);
    Vector3D velocity = rotationToVelocityFromAnomaly.applyTo(anomalyFrameVelocity);

    if (frame.equals(BODY_INERTIAL_FRAME)) {
      radius = rotationToInertialFromVelocity.applyTo(radius);
      velocity = rotationToInertialFromVelocity.applyTo(velocity);
    }

    motionVectorsMap.putData(
        new MotionVectors(
            orbit.getBody(), velocity, radius, rotationToInertialFromVelocity, epoch, frame));
    return this;
  }

  public MotionVectorBuilder buildVectors(OrbitalVectors orbitalVectors) {
    Orbit orbit = new OrbitBuilder().buildFromVectors(orbitalVectors).getOrbit();
    return buildVectors(
        orbit, orbitalVectors.getTrueAnomaly(), orbitalVectors.getEpoch(), BODY_INERTIAL_FRAME);
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

  private Vector3D anomalyVelocityVector(Orbit orbit, double trueAnomaly) {
    var verticalVelocity = computeVerticalVelocityComponent(orbit, trueAnomaly);
    var tangentialVelocity = computeTangentialVelocityComponent(orbit, trueAnomaly);
    return new Vector3D(new double[] {verticalVelocity, tangentialVelocity, 0});
  }

  private Vector3D anomalyRadiusVector(Orbit orbit, double trueAnomaly) {
    var semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    var eccentricity = orbit.getDataFor(ECCENTRICITY);
    var radius =
        semiMajorAxis
            * (1 - Math.pow(eccentricity, 2))
            / (1 + eccentricity * Math.cos(trueAnomaly));
    return new Vector3D(new double[] {radius, 0, 0});
  }

  private double computeTangentialVelocityComponent(Orbit orbit, double trueAnomaly) {
    var mu = orbit.getBody().getMu();
    var semiLatusRectum = semiLatusRectum(orbit);
    var eccentricity = orbit.getDataFor(ECCENTRICITY);
    return Math.sqrt(mu / semiLatusRectum) * (1 + eccentricity * Math.cos(trueAnomaly));
  }

  private double computeVerticalVelocityComponent(Orbit orbit, double trueAnomaly) {
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
