package org.example.equations.method.vector;

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
    return new Rotation(Z_AXIS,-velocityAngle,VECTOR_OPERATOR);
  }

  public Rotation getInertialFromMotion(){
    var finalZAngle = argumentPE + anomalyAngle + velocityAngle;
    return new Rotation(ZXZ,VECTOR_OPERATOR,-finalZAngle,-inclination,-rightAscension);
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
