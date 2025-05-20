package org.example.equations.method.vector;

import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;

import java.util.Optional;

import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.VECTOR_OPERATOR;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationOrder.ZXZ;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

public class AngleTransform {
  private static final double TOLERANCE = 1e-6;
  private static final Vector3D X_AXIS = Vector3D.PLUS_I;
  private static final Vector3D Y_AXIS = Vector3D.PLUS_J;
  private static final Vector3D Z_AXIS = Vector3D.PLUS_K;
  private static final Plane XY_PLANE = new Plane(Z_AXIS, TOLERANCE);
  @Getter private Rotation rotation;

  public AngleTransform setVectorsToVelocityFrame(Vector3D velocity, Vector3D radius) {
    rotation = getMotionFrameFromAnomalyFrame(velocity, radius);
    radius = invert(radius);
    return this;
  }

  public Vector3D invert(Vector3D vector) {
    return vector.negate();
  }

  public Rotation getMotionFrameFromAnomalyFrame(Vector3D velocity, Vector3D radius) {
    boolean belowPI = velocity.getY() >= 0;
    double angle =
        belowPI ? Vector3D.angle(velocity, radius) : 2 * Math.PI - Vector3D.angle(velocity, radius);
    return new Rotation(Z_AXIS, -angle, VECTOR_OPERATOR);
  }

  public Rotation getInertialFrameFromMotionFrame(
      Vector3D velocity, Vector3D bodyDistance, Orbit orbit, double trueAnomaly) {
    var motionToAnomaly = getAnomalyFrameFromMotionFrame(velocity, bodyDistance);
    var anomalyToInertial =
        getInertialFromAnomalyFrame(
            orbit.getDataFor(RIGHT_ASCENSION),
            orbit.getDataFor(INCLINATION),
            orbit.getDataFor(ARGUMENT_PE),
            trueAnomaly);
    return anomalyToInertial.compose(motionToAnomaly,VECTOR_OPERATOR);
  }

  public Rotation getAnomalyFrameFromMotionFrame(Vector3D velocity, Vector3D bodyDistance) {
    var radius = bodyDistance.negate();
    boolean belowPI = radius.getY() <= 0;
    double angle =
        belowPI ? Vector3D.angle(velocity, radius) : 2 * Math.PI - Vector3D.angle(velocity, radius);
    return new Rotation(Z_AXIS, angle, VECTOR_OPERATOR);
  }

  public Rotation getInertialFromAnomalyFrame(
      double rightAscension, double inclination, double argumentPE, double trueAnomaly) {
    Rotation craftToPlanar = getPlanarFromAnomalyFrame(trueAnomaly);
    Rotation planarToInertial = getInertialFromPlanar(rightAscension, inclination, argumentPE);
    return planarToInertial.compose(craftToPlanar, VECTOR_OPERATOR);
  }

  private static Rotation getPlanarFromAnomalyFrame(double trueAnomaly) {
    return new Rotation(Z_AXIS, -trueAnomaly, VECTOR_OPERATOR);
  }

  private static Rotation getInertialFromPlanar(
      double rightAscension, double inclination, double argumentPE) {
    return new Rotation(ZXZ, VECTOR_OPERATOR, -argumentPE, -inclination, -rightAscension);
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
}
