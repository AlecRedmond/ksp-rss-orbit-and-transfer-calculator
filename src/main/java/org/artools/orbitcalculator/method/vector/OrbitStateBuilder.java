package org.artools.orbitcalculator.method.vector;

import static org.apache.commons.math3.util.FastMath.atan2;
import static org.apache.commons.math3.util.FastMath.sin;
import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;

import java.time.Instant;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;

@Getter
public class OrbitStateBuilder {
  private OrbitStateBuilder() {}

  public static OrbitalState buildFromKeplerOrbit(KeplerOrbit orbit, Orrery orrery) {
    Planet centralBody = orrery.getPlanetByType(orbit.getCentralBodyType());
    double angularMomentum = calculateAngularMomentum(orbit, centralBody);
    Rotation rotationToInertial = calculateRotationToInertial(orbit);
    Vector3D position =
        calculateInertialPosition(angularMomentum, orbit, rotationToInertial, centralBody);
    Vector3D velocity =
        calculateInertialVelocity(angularMomentum, orbit, rotationToInertial, centralBody);
    return buildFromVelocityAndPositionVectors(
        centralBody, position, velocity, orbit.getTimestamp().toInstant());
  }

  private static double calculateAngularMomentum(KeplerOrbit orbit, Planet centralBody) {
    double mu = centralBody.getMu();
    double sma = orbit.getData(SEMI_MAJOR_AXIS);
    double eccentricity = orbit.getData(ECCENTRICITY);
    return Math.sqrt(mu * sma * (1 - Math.pow(eccentricity, 2)));
  }

  private static Rotation calculateRotationToInertial(KeplerOrbit orbit) {
    double lngAscendingNode = orbit.getData(LONGITUDE_ASCENDING_NODE);
    double inclination = orbit.getData(INCLINATION);
    double argumentOfPeriapsis = orbit.getData(ARGUMENT_OF_PERIAPSIS);
    return new Rotation(
        RotationOrder.ZXZ,
        RotationConvention.FRAME_TRANSFORM,
        -argumentOfPeriapsis,
        -inclination,
        -lngAscendingNode);
  }

  private static Vector3D calculateInertialPosition(
      double angularMomentum, KeplerOrbit orbit, Rotation rotationToInertial, Planet centralBody) {
    double mu = centralBody.getMu();
    double trueAnomaly = orbit.getData(TRUE_ANOMALY);
    double eccentricity = orbit.getData(ECCENTRICITY);

    double radiusMagnitude = calculateCurrentRadius(angularMomentum, mu, eccentricity, trueAnomaly);
    double xPerifocal = radiusMagnitude * Math.cos(trueAnomaly);
    double yPerifocal = radiusMagnitude * Math.sin(trueAnomaly);
    Vector3D perifocalPosition = new Vector3D(new double[] {xPerifocal, yPerifocal, 0.0});
    return rotationToInertial.applyTo(perifocalPosition);
  }

  private static Vector3D calculateInertialVelocity(
      double angularMomentum, KeplerOrbit orbit, Rotation rotationToInertial, Planet centralBody) {
    double mu = centralBody.getMu();
    double trueAnomaly = orbit.getData(TRUE_ANOMALY);
    double eccentricity = orbit.getData(ECCENTRICITY);

    double xVPerifocal = -1 * (mu / angularMomentum) * Math.sin(trueAnomaly);
    double yVPerifocal = (mu / angularMomentum) * (eccentricity + Math.cos(trueAnomaly));
    Vector3D perifocalVelocity = new Vector3D(new double[] {xVPerifocal, yVPerifocal, 0.0});
    return rotationToInertial.applyTo(perifocalVelocity);
  }

  private static OrbitalState buildFromVelocityAndPositionVectors(
      Planet centralBody, Vector3D position, Vector3D velocity, Instant epoch) {
    Vector3D momentum = position.crossProduct(velocity);
    Vector3D eccentricity = getEccentricity(velocity, momentum, centralBody, position);
    Vector3D ascendingNodeVector = Vector3D.PLUS_K.crossProduct(momentum);
    double semiMajorAxis = getSemiMajorAxis(momentum, centralBody, eccentricity);
    double inclination = getInclination(momentum);
    double rightAscension = getLongitudeAscendingNode(ascendingNodeVector);
    double argumentPE = getArgumentPE(ascendingNodeVector, eccentricity);
    double trueAnomaly = getTrueAnomaly(eccentricity, position, velocity);
    double eccentricAnomaly = getEccentricAnomaly(eccentricity, trueAnomaly);
    double meanAnomaly = getMeanAnomaly(eccentricAnomaly, eccentricity);
    return OrbitalState.builder()
        .centralBody(centralBody)
        .position(position)
        .velocity(velocity)
        .momentum(momentum)
        .eccentricity(eccentricity)
        .semiMajorAxis(semiMajorAxis)
        .longitudeAscendingNode(rightAscension)
        .inclination(inclination)
        .argumentPE(argumentPE)
        .trueAnomaly(trueAnomaly)
        .eccentricAnomaly(eccentricAnomaly)
        .meanAnomaly(meanAnomaly)
        .epoch(epoch)
        .build();
  }

  private static double calculateCurrentRadius(
      double angularMomentum, double mu, double eccentricity, double trueAnomaly) {
    return Math.pow(angularMomentum, 2) / (mu * (1 + eccentricity * Math.cos(trueAnomaly)));
  }

  private static Vector3D getEccentricity(
      Vector3D velocity, Vector3D momentum, Planet planet, Vector3D position) {
    Vector3D vector1 = velocity.crossProduct(momentum).scalarMultiply(1 / planet.getMu());
    Vector3D vector2 = position.scalarMultiply(1 / position.getNorm());
    return vector1.subtract(vector2);
  }

  private static double getSemiMajorAxis(
      Vector3D momentumVector, Planet planet, Vector3D eccentricityVector) {
    double eccentricity = eccentricityVector.getNorm();
    if (eccentricity == 1) {
      return Double.MAX_VALUE;
    }
    double eccentricitySquared = Math.pow(eccentricity, 2);
    double momentumSquared = Math.pow(momentumVector.getNorm(), 2);
    double semiLatusRectum = momentumSquared / planet.getMu();
    return semiLatusRectum / (1 - eccentricitySquared);
  }

  private static double getInclination(Vector3D momentum) {
    double cosI = Vector3D.PLUS_K.dotProduct(momentum) / momentum.getNorm();
    return Math.acos(cosI);
  }

  private static double getLongitudeAscendingNode(Vector3D ascendingNodeVector) {
    double nodeI = Vector3D.PLUS_I.dotProduct(ascendingNodeVector) / ascendingNodeVector.getNorm();
    double omega = Math.acos(nodeI);
    double nodeJ = Vector3D.PLUS_J.dotProduct(ascendingNodeVector) / ascendingNodeVector.getNorm();
    return nodeJ >= 0 ? omega : 2 * Math.PI - omega;
  }

  private static double getArgumentPE(Vector3D ascendingNodeVector, Vector3D eccentricity) {
    double cosOmega =
        ascendingNodeVector.dotProduct(eccentricity)
            / (ascendingNodeVector.getNorm() * eccentricity.getNorm());
    double omega = Math.acos(cosOmega);
    return eccentricity.getZ() >= 0 ? omega : 2 * Math.PI - omega;
  }

  private static double getTrueAnomaly(
      Vector3D eccentricity, Vector3D position, Vector3D velocity) {
    double cosAnomaly =
        eccentricity.dotProduct(position) / ((eccentricity.getNorm()) * position.getNorm());
    double anomaly = Math.acos(cosAnomaly);
    double quadrantCheck = position.dotProduct(velocity);
    return quadrantCheck >= 0 ? anomaly : 2 * Math.PI - anomaly;
  }

  private static double getEccentricAnomaly(Vector3D eccentricityVector, double trueAnomaly) {
    double eccentricity = eccentricityVector.getNorm();
    if (eccentricity >= 1) {
      return 0;
    }
    double sinE = Math.sqrt(1 - Math.pow(eccentricity, 2)) * Math.sin(trueAnomaly);
    double cosE = eccentricity + Math.cos(trueAnomaly);
    double eAnomaly = atan2(sinE, cosE);
    return eAnomaly >= 0 ? eAnomaly : 2 * Math.PI - eAnomaly;
  }

  private static double getMeanAnomaly(double eccentricAnomaly, Vector3D eccentricityVector) {
    double eccentricity = eccentricityVector.getNorm();
    if (eccentricity >= 1) {
      return 0;
    }
    return eccentricAnomaly - (eccentricAnomaly * sin(eccentricAnomaly));
  }

  public static OrbitalState buildFromMotionState(MotionState satelliteState, Planet centralBody) {
    Instant epoch = satelliteState.getEpoch();
    Vector3D velocity = calculateRelativeVelocity(satelliteState, centralBody.getMotionState());
    Vector3D position = calculateRelativePosition(satelliteState, centralBody.getMotionState());
    return buildFromVelocityAndPositionVectors(centralBody, position, velocity, epoch);
  }

  private static Vector3D calculateRelativeVelocity(
      MotionState satelliteState, MotionState centralBodyState) {
    return satelliteState.getVelocity().subtract(centralBodyState.getVelocity());
  }

  private static Vector3D calculateRelativePosition(
      MotionState satelliteState, MotionState centralBodyState) {
    return satelliteState.getPosition().subtract(centralBodyState.getPosition());
  }
}
