package org.example.equations.method.vector;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;

import java.util.Optional;

import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.FRAME_TRANSFORM;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.VECTOR_OPERATOR;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationOrder.ZXZ;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

public class AngleTransform {
  private static final double TOLERANCE = 1e-6;
  private static final Vector3D Z_AXIS = Vector3D.PLUS_K;
  private double velocityAngle = 0;
  private double anomalyAngle = 0;
  private double argumentPE = 0;
  private double inclination = 0;
  private double rightAscension = 0;
  private Rotation rotation;

  public AngleTransform setVelocityAngle(Vector3D velocity, Vector3D radius){
    velocityAngle = Vector3D.angle(velocity,radius);
    return this;
  }

  public AngleTransform setAnomalyAngle(double trueAnomaly){
    anomalyAngle = trueAnomaly;
    return this;
  }

  public AngleTransform setOrbitAngles(Orbit orbit){
    rightAscension = orbit.getDataFor(RIGHT_ASCENSION);
    inclination = orbit.getDataFor(INCLINATION);
    argumentPE = orbit.getDataFor(ARGUMENT_PE);
    return this;
  }

  public Rotation getToMotionInitializer(){
    return new Rotation(Z_AXIS,velocityAngle,FRAME_TRANSFORM);
  }

  public Rotation getInertialFromMotion(){
    var finalZAngle = argumentPE + anomalyAngle + velocityAngle;
    return new Rotation(ZXZ,FRAME_TRANSFORM,-finalZAngle,-inclination,-rightAscension);
  }

  private static Rotation getInertialFromPlanar(
      double rightAscension, double inclination, double argumentPE) {
    return new Rotation(ZXZ, FRAME_TRANSFORM, -argumentPE, -inclination, -rightAscension);
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
