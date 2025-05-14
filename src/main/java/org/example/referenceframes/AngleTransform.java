package org.example.referenceframes;

import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.*;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationOrder.*;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;

public class AngleTransform {
  private static final double TOLERANCE = 1e-6;

  /** rotates the vector to its equivalent in a plane where all Z values in the orbit equal 0 */
  public Vector3D toOrbitalFrame(Vector3D vector, Orbit orbit) {
    return toOrbitalFrame(
        vector,
        orbit.getDataFor(RIGHT_ASCENSION),
        orbit.getDataFor(INCLINATION),
        orbit.getDataFor(ARGUMENT_PE));
  }

  protected Vector3D toOrbitalFrame(
      Vector3D vector, double rightAscension, double inclination, double argumentPE) {
    Rotation rotation = new Rotation(ZXZ, FRAME_TRANSFORM, rightAscension, inclination, argumentPE);
    return rotation.applyTo(vector);
  }

  /** rotates the vector from the orbital to the body-centric (e.g. Earth-Centred) frame. */
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

  public Line intercept(Orbit orbitA, Orbit orbitB) {
    Plane planeA = orbitalPlane(orbitA);
    Plane planeB = orbitalPlane(orbitB);
    return planeA.intersection(planeB);
  }

  protected Plane orbitalPlane(Orbit orbit) {
    return orbitalPlane(
        orbit.getDataFor(RIGHT_ASCENSION),
        orbit.getDataFor(INCLINATION),
        orbit.getDataFor(ARGUMENT_PE));
  }

  protected Plane orbitalPlane(double rightAscension, double inclination, double argumentPE) {
    Vector3D normalVector =
        toInertialFrame(
            new Vector3D(new double[] {0, 0, 1}), rightAscension, inclination, argumentPE);
    return new Plane(normalVector, TOLERANCE);
  }
}
