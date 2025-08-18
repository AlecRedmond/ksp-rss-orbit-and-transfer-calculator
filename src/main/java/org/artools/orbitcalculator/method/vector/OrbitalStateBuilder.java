package org.artools.orbitcalculator.method.vector;

import static org.apache.commons.math3.util.FastMath.atan2;
import static org.apache.commons.math3.util.FastMath.sin;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;

@Getter
@NoArgsConstructor
public class OrbitalStateBuilder {
  private OrbitalState vectors = new OrbitalState();

  public OrbitalStateBuilder(MotionState satelliteState, Planet centralBody){
    buildVectors(satelliteState,centralBody);
  }
  private void buildVectors(MotionState satelliteState, Planet centralBody) {
    Instant epoch = satelliteState.getEpoch();
    Vector3D velocity = getRelativeVelocity(satelliteState,centralBody.getMotionState());
    Vector3D position = getRelativePosition(satelliteState,centralBody.getMotionState());
    buildVectors(position, velocity, centralBody, epoch);
  }

  private static Vector3D getRelativeVelocity(MotionState satelliteState, MotionState centralBodyState) {
      return satelliteState.getVelocity().subtract(centralBodyState.getVelocity());
  }

  private static Vector3D getRelativePosition(MotionState satelliteState, MotionState centralBodyState) {
    return satelliteState.getPosition().subtract(centralBodyState.getPosition());
  }

  private void buildVectors(Vector3D position, Vector3D velocity, Planet centralBody, Instant epoch) {
    Vector3D momentum = position.crossProduct(velocity);
    Vector3D eccentricity = getEccentricity(velocity, momentum, centralBody, position);
    Vector3D ascendingNodeVector = Vector3D.PLUS_K.crossProduct(momentum);
    double semiMajorAxis = getSemiMajorAxis(momentum, centralBody, eccentricity);
    double inclination = getInclination(momentum);
    double rightAscension = getRightAscension(ascendingNodeVector);
    double argumentPE = getArgumentPE(ascendingNodeVector, eccentricity);
    double trueAnomaly = getTrueAnomaly(eccentricity, position, velocity);
    double eccentricAnomaly = getEccentricAnomaly(eccentricity, trueAnomaly);
    double meanAnomaly = getMeanAnomaly(eccentricAnomaly, eccentricity);
    vectors =
        OrbitalState.builder()
            .centralBody(centralBody)
            .position(position)
            .velocity(velocity)
            .momentum(momentum)
            .eccentricity(eccentricity)
            .semiMajorAxis(semiMajorAxis)
            .rightAscension(rightAscension)
            .inclination(inclination)
            .argumentPE(argumentPE)
            .trueAnomaly(trueAnomaly)
            .eccentricAnomaly(eccentricAnomaly)
            .meanAnomaly(meanAnomaly)
            .epoch(epoch)
            .build();
  }

  private Vector3D getEccentricity(
          Vector3D velocity, Vector3D momentum, Planet planet, Vector3D position) {
    var vector1 = velocity.crossProduct(momentum).scalarMultiply(1 / planet.getMu());
    var vector2 = position.scalarMultiply(1 / position.getNorm());
    return vector1.subtract(vector2);
  }

  private double getSemiMajorAxis(Vector3D momentumVector, Planet planet, Vector3D eccentricityVector) {
    double eccentricity = eccentricityVector.getNorm();
    if (eccentricity == 1) {
      return Double.MAX_VALUE;
    }
    double eccentricitySquared = Math.pow(eccentricity, 2);
    double momentumSquared = Math.pow(momentumVector.getNorm(), 2);
    double semiLatusRectum = momentumSquared / planet.getMu();
    return semiLatusRectum / (1 - eccentricitySquared);
  }

  private double getInclination(Vector3D momentum) {
    var cosI = Vector3D.PLUS_K.dotProduct(momentum) / momentum.getNorm();
    return Math.acos(cosI);
  }

  private double getRightAscension(Vector3D ascendingNodeVector) {
    var nodeI = Vector3D.PLUS_I.dotProduct(ascendingNodeVector) / ascendingNodeVector.getNorm();
    var omega = Math.acos(nodeI);
    var nodeJ = Vector3D.PLUS_J.dotProduct(ascendingNodeVector) / ascendingNodeVector.getNorm();
    return nodeJ >= 0 ? omega : 2 * Math.PI - omega;
  }

  private double getArgumentPE(Vector3D ascendingNodeVector, Vector3D eccentricity) {
    var cosOmega =
        ascendingNodeVector.dotProduct(eccentricity)
            / (ascendingNodeVector.getNorm() * eccentricity.getNorm());
    var omega = Math.acos(cosOmega);
    return eccentricity.getZ() >= 0 ? omega : 2 * Math.PI - omega;
  }

  private double getTrueAnomaly(Vector3D eccentricity, Vector3D position, Vector3D velocity) {
    var cosAnomaly =
        eccentricity.dotProduct(position) / ((eccentricity.getNorm()) * position.getNorm());
    var anomaly = Math.acos(cosAnomaly);
    var quadrantCheck = position.dotProduct(velocity);
    return quadrantCheck >= 0 ? anomaly : 2 * Math.PI - anomaly;
  }

  private double getEccentricAnomaly(Vector3D eccentricityVector, double trueAnomaly) {
    var eccentricity = eccentricityVector.getNorm();
    if (eccentricity >= 1) {
      return 0;
    }
    var sinE = Math.sqrt(1 - Math.pow(eccentricity, 2)) * Math.sin(trueAnomaly);
    var cosE = eccentricity + Math.cos(trueAnomaly);
    var eAnomaly = atan2(sinE, cosE);
    return eAnomaly >= 0 ? eAnomaly : 2 * Math.PI - eAnomaly;
  }

  private double getMeanAnomaly(double eccentricAnomaly, Vector3D eccentricityVector) {
    var eccentricity = eccentricityVector.getNorm();
    if (eccentricity >= 1) {
      return 0;
    }
    return eccentricAnomaly - (eccentricAnomaly * sin(eccentricAnomaly));
  }

}
