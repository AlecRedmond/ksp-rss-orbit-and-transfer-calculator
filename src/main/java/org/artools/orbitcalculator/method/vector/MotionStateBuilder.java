package org.artools.orbitcalculator.method.vector;

import java.time.Instant;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.application.writeableorbit.keplerianelements.Kepler;

@Getter
public class MotionStateBuilder {
  private MotionState vectors = new MotionState();

  private static FrameTransform getTransform(Orbit orbit, double trueAnomaly) {
    return new FrameTransform().setAnomalyAngle(trueAnomaly).setOrbitAngles(orbit);
  }

  public MotionStateBuilder buildVectors(Orbit orbit, double trueAnomaly, Instant epoch) {
    FrameTransform transform = getTransform(orbit, trueAnomaly);
    Vector3D velocity = buildVelocityVector(orbit, trueAnomaly, transform);
    Vector3D radius = buildRadiusVector(orbit, trueAnomaly, transform);
    vectors = new MotionState(velocity, radius, epoch);
    return this;
  }

  private Vector3D buildVelocityVector(
      Orbit orbit, double trueAnomaly, FrameTransform frameTransform) {
    var verticalVelocity = computeVerticalVelocityComponent(orbit, trueAnomaly);
    var tangentialVelocity = computeTangentialVelocityComponent(orbit, trueAnomaly);
    var anomalyVector = new Vector3D(new double[] {verticalVelocity, tangentialVelocity, 0});
    var rotation = frameTransform.getRotationToInertialFromAnomaly();
    return rotation.applyTo(anomalyVector);
  }

  private Vector3D buildRadiusVector(Orbit orbit, double trueAnomaly, FrameTransform transform) {
    var semiMajorAxis = orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS);
    var eccentricity = orbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY);
    var radius =
        semiMajorAxis
            * (1 - Math.pow(eccentricity, 2))
            / (1 + eccentricity * Math.cos(trueAnomaly));
    var anomalyRadius = new Vector3D(new double[] {radius, 0, 0});
    var rotation = transform.getRotationToInertialFromAnomaly();
    return rotation.applyTo(anomalyRadius);
  }

  private double computeTangentialVelocityComponent(Orbit orbit, double trueAnomaly) {
    var mu = orbit.getBody().getMu();
    var semiLatusRectum = semiLatusRectum(orbit);
    var eccentricity = orbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY);
    return Math.sqrt(mu / semiLatusRectum) * (1 + eccentricity * Math.cos(trueAnomaly));
  }

  private double computeVerticalVelocityComponent(Orbit orbit, double trueAnomaly) {
    var mu = orbit.getBody().getMu();
    var semiLatusRectum = semiLatusRectum(orbit);
    var eccentricity = orbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY);
    return Math.sqrt(mu / semiLatusRectum) * (eccentricity * Math.sin(trueAnomaly));
  }

  private double semiLatusRectum(Orbit orbit) {
    return semiLatusRectum(orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS), orbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY));
  }

  private double semiLatusRectum(double semiMajorAxis, double eccentricity) {
    return semiMajorAxis * (1 - Math.pow(eccentricity, 2));
  }
}
