package org.example.equations.method.vector;

import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.FRAME_TRANSFORM;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationOrder.ZXZ;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.Optional;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;

public class AngleTransform {
  private static final double TOLERANCE = 1e-6;
  private static final Vector3D Z_AXIS = Vector3D.PLUS_K;
  private double velocityAngle = 0;
  private double anomalyAngle = 0;
  private double argumentPE = 0;
  private double inclination = 0;
  private double rightAscension = 0;

  public AngleTransform setVelocityAngle(Vector3D velocity, Vector3D radius) {
    velocityAngle = Vector3D.angle(velocity, radius);
    return this;
  }

  public AngleTransform setAnomalyAngle(double trueAnomaly) {
    anomalyAngle = trueAnomaly;
    return this;
  }

  public AngleTransform setOrbitAngles(Orbit orbit) {
    rightAscension = orbit.getDataFor(RIGHT_ASCENSION);
    inclination = orbit.getDataFor(INCLINATION);
    argumentPE = orbit.getDataFor(ARGUMENT_PE);
    return this;
  }

  /**
   * Returns a rotation to the Motion (Velocity-Centric) frame, where the X-Axis points in the
   * direction of the current tangential Velocity. The Y axis will describe the "Radial" direction
   * vector, and the Z axis the Orbital Normal.
   */
  public Rotation getToMotionInitializer() {
    return new Rotation(Z_AXIS, velocityAngle, FRAME_TRANSFORM);
  }

  /**
   * Returns the angle to the body-centric Inertial frame from the Motion (Velocity-centric) frame.
   */
  public Rotation getInertialFromMotion() {
    var finalZAngle = argumentPE + anomalyAngle + velocityAngle;
    return new Rotation(ZXZ, FRAME_TRANSFORM, -finalZAngle, -inclination, -rightAscension);
  }

  /**
   * Returns the line at which two orbits in the same frame intersect each other. Will return the
   * line in inertial frame.
   */
  public Optional<Line> intersect(Orbit orbitA, Orbit orbitB) {
    Plane planeA = orbitalPlane(orbitA);
    Plane planeB = orbitalPlane(orbitB);
    return Optional.ofNullable(planeA.intersection(planeB));
  }

  protected Plane orbitalPlane(Orbit orbit) {
    return orbitalPlane(
        orbit.getDataFor(RIGHT_ASCENSION),
        orbit.getDataFor(INCLINATION),
        orbit.getDataFor(ARGUMENT_PE));
  }

  protected Plane orbitalPlane(double rightAscension, double inclination, double argumentPE) {
    Rotation planarToInertial = getInertialFromPlanar(rightAscension, inclination, argumentPE);
    Vector3D normalVector = planarToInertial.applyTo(Z_AXIS);
    return new Plane(normalVector, TOLERANCE);
  }

  private static Rotation getInertialFromPlanar(
      double rightAscension, double inclination, double argumentPE) {
    return new Rotation(ZXZ, FRAME_TRANSFORM, -argumentPE, -inclination, -rightAscension);
  }
}
