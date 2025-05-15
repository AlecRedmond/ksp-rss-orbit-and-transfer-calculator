package org.example.equations.method.referenceframes;

import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.*;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationOrder.*;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.Optional;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;

public class AngleTransform {
  private static final double TOLERANCE = 1e-6;
  private static final Vector3D X_AXIS = Vector3D.PLUS_I;
  private static final Vector3D Z_AXIS = Vector3D.PLUS_K;

  /**
   * rotates the vector from the Peri-Focal to the Body-centric (e.g. Earth-Centred) Inertial frame.
   */
  public Vector3D toInertialFrame(Vector3D vector, Orbit orbit) {
    return toInertialFrame(
        vector,
        orbit.getDataFor(RIGHT_ASCENSION),
        orbit.getDataFor(INCLINATION),
        orbit.getDataFor(ARGUMENT_PE));
  }

  protected Vector3D toInertialFrame(
      Vector3D vector, double rightAscension, double inclination, double argumentPE) {
    Rotation rotation =
        new Rotation(ZXZ, FRAME_TRANSFORM, -argumentPE, -inclination, -rightAscension);
    return rotation.applyTo(vector);
  }

  /** Returns the craft-oriented radius vector in the Perifocal frame. */
  public Vector3D perifocalRadiusVector(double radius, double trueAnomaly) {
    Vector3D radiusVector = X_AXIS.scalarMultiply(radius);
    Rotation rotation = new Rotation(Z_AXIS, -trueAnomaly, FRAME_TRANSFORM);
    return rotation.applyTo(radiusVector);
  }

  /** Transforms the velocity vector from the Craft-oriented to the Perifocal frame. */
  public Vector3D perifocalVelocityVector(Vector3D velocityVector, double trueAnomaly) {
    Rotation rotation = new Rotation(Z_AXIS, -trueAnomaly, FRAME_TRANSFORM);
    return rotation.applyTo(velocityVector);
  }

  /**
   * Returns the true anomalies of the base orbit where it intersects the plane of the target orbit.
   * If the orbits are co-planar, returns empty.
   */
  public Optional<double[]> intersectTrueAnomaly(Orbit base, Orbit target) {
    var intersectLine = intersect(base, target);
    if (intersectLine.isEmpty()) {
      return Optional.empty();
    }
    var inertialIntersectDirection = intersectLine.get().getDirection();
    var perifocalIntersectDirection = toPerifocalFrame(inertialIntersectDirection, base);
    var firstTrueAnomaly = Vector3D.angle(X_AXIS, perifocalIntersectDirection);
    // Vector3D.angle is ||absolute||; negative Y values mean anomaly is negative
    if (perifocalIntersectDirection.getY() < 0) {
      firstTrueAnomaly = (Math.PI - firstTrueAnomaly) % (2 * Math.PI);
    }
    var secondTrueAnomaly = (firstTrueAnomaly + Math.PI) % (2 * Math.PI);
    return Optional.of(new double[] {firstTrueAnomaly, secondTrueAnomaly});
  }

  /** Returns the line at which two orbits in the same frame intersect each other. */
  public Optional<Line> intersect(Orbit orbitA, Orbit orbitB) {
    Plane planeA = orbitalPlane(orbitA);
    Plane planeB = orbitalPlane(orbitB);
    return Optional.ofNullable(planeA.intersection(planeB));
  }

  /**
   * rotates the vector to its equivalent in the Perifocal frame, where X is positive in the
   * direction of periapsis and Z is normal to the orbit.
   */
  public Vector3D toPerifocalFrame(Vector3D vector, Orbit orbit) {
    return toPerifocalFrame(
        vector,
        orbit.getDataFor(RIGHT_ASCENSION),
        orbit.getDataFor(INCLINATION),
        orbit.getDataFor(ARGUMENT_PE));
  }

  protected Plane orbitalPlane(Orbit orbit) {
    return orbitalPlane(
        orbit.getDataFor(RIGHT_ASCENSION),
        orbit.getDataFor(INCLINATION),
        orbit.getDataFor(ARGUMENT_PE));
  }

  protected Vector3D toPerifocalFrame(
      Vector3D vector, double rightAscension, double inclination, double argumentPE) {
    Rotation rotation = new Rotation(ZXZ, FRAME_TRANSFORM, rightAscension, inclination, argumentPE);
    return rotation.applyTo(vector);
  }

  protected Plane orbitalPlane(double rightAscension, double inclination, double argumentPE) {
    Vector3D normalVector = toInertialFrame(Z_AXIS, rightAscension, inclination, argumentPE);
    return new Plane(normalVector, TOLERANCE);
  }
}
