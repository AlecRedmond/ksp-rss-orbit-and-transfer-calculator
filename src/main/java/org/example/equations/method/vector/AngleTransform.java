package org.example.equations.method.vector;

import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.*;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationOrder.*;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.Optional;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.CraftVectors;
import org.example.equations.application.vector.ReferenceFrame;

public class AngleTransform {
  private static final double TOLERANCE = 1e-6;
  private static final Vector3D X_AXIS = Vector3D.PLUS_I;
  private static final Vector3D Z_AXIS = Vector3D.PLUS_K;

  public CraftVectors rotateCraftVectors(
      CraftVectors craftVectors, ReferenceFrame finalFrame, Orbit orbit, double trueAnomaly) {
    if (craftVectors.getFrame().equals(finalFrame)) {
      return craftVectors;
    }
    var rotation = getRotationTransform(craftVectors.getFrame(), finalFrame, orbit, trueAnomaly);
    return rotateAll(craftVectors, rotation, finalFrame);
  }

  private Rotation getRotationTransform(
      ReferenceFrame initialFrame, ReferenceFrame finalFrame, Orbit orbit, double trueAnomaly) {

    switch (finalFrame) {
      case CRAFT -> {
        return rotateToCraftFrame(initialFrame, orbit, trueAnomaly);
      }
      case PLANAR -> {
        return rotateToPlanarFrame(initialFrame, orbit, trueAnomaly);
      }
      case INERTIAL -> {
        return rotateToInertialFrame(initialFrame, orbit, trueAnomaly);
      }
    }

    return null;
  }

  private CraftVectors rotateAll(
      CraftVectors craftVectors, Rotation rotation, ReferenceFrame finalFrame) {
    var velocity = craftVectors.getVelocity();
    var position = craftVectors.getPosition();
    var momentum = craftVectors.getMomentum();
    craftVectors.setVelocity(rotation.applyTo(velocity));
    craftVectors.setPosition(rotation.applyTo(position));
    craftVectors.setMomentum(rotation.applyTo(momentum));
    craftVectors.setFrame(finalFrame);
    return craftVectors;
  }

  private Rotation rotateToCraftFrame(
      ReferenceFrame initialFrame, Orbit orbit, double trueAnomaly) {
    switch (initialFrame) {
      case CRAFT -> {
        return null;
      }
      case PLANAR -> {
        return rotateByTrueAnomaly(-trueAnomaly);
      }
      case INERTIAL -> {
        return rotateByTrueAnomaly(-trueAnomaly)
            .compose(rotateInertialToPlanar(orbit, false), FRAME_TRANSFORM);
      }
    }
    return null;
  }

  private Rotation rotateToPlanarFrame(
      ReferenceFrame initialFrame, Orbit orbit, double trueAnomaly) {
    switch (initialFrame) {
      case CRAFT -> {
        return rotateByTrueAnomaly(trueAnomaly);
      }
      case PLANAR -> {
        return null;
      }
      case INERTIAL -> {
        return rotateInertialToPlanar(orbit, false);
      }
    }
    return null;
  }

  private Rotation rotateToInertialFrame(
      ReferenceFrame initialFrame, Orbit orbit, double trueAnomaly) {
    switch (initialFrame) {
      case CRAFT -> {
        return rotateInertialToPlanar(orbit, true)
            .compose(rotateByTrueAnomaly(trueAnomaly), FRAME_TRANSFORM);
      }
      case PLANAR -> {
        return rotateInertialToPlanar(orbit, true);
      }
      case INERTIAL -> {
        return null;
      }
    }
    return null;
  }

  private Rotation rotateByTrueAnomaly(double trueAnomaly) {
    return new Rotation(Z_AXIS, trueAnomaly, FRAME_TRANSFORM);
  }

  private Rotation rotateInertialToPlanar(Orbit orbit, boolean reversed) {

    var rightAscension = orbit.getDataFor(RIGHT_ASCENSION);
    var inclination = orbit.getDataFor(INCLINATION);
    var argumentPE = orbit.getDataFor(ARGUMENT_PE);

    return reversed
        ? new Rotation(ZXZ, FRAME_TRANSFORM, -argumentPE, -inclination, -rightAscension)
        : new Rotation(ZXZ, FRAME_TRANSFORM, rightAscension, inclination, argumentPE);
  }

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

  /** Transforms a vector from the Craft-oriented to the Perifocal frame. */
  public Vector3D craftToPerifocalTransform(Vector3D craftVector, double trueAnomaly) {
    Rotation rotation = new Rotation(Z_AXIS, -trueAnomaly, FRAME_TRANSFORM);
    return rotation.applyTo(craftVector);
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
    var inertialDirection = intersectLine.get().getDirection();
    var perifocalDirection = toPerifocalFrame(inertialDirection, base);
    // Vector3D.angle is ||absolute||; negative Y values mean anomaly is negative
    var firstTrueAnomaly =
        perifocalDirection.getY() >= 0
            ? Vector3D.angle(X_AXIS, perifocalDirection)
            : Math.PI - Vector3D.angle(X_AXIS, perifocalDirection);
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
