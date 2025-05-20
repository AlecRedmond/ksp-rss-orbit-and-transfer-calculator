package org.example.equations.method.vector;

import static org.apache.commons.math3.util.FastMath.atan2;
import static org.apache.commons.math3.util.FastMath.sin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.CraftVectors;
import org.example.equations.application.vector.OrbitalVectors;
import org.example.equations.method.OrbitBuilder;

@Getter
@NoArgsConstructor
public class OrbitalVectorController {
  private OrbitalVectors vectors;

  public OrbitalVectorController buildVectors(CraftVectors craftVectors) {
    Body body = craftVectors.getBody();
    Rotation rotation = craftVectors.getRotationToInertial();
    Vector3D velocity = rotation.applyTo(new Vector3D(1, craftVectors.getVelocity()));
    Vector3D position = rotation.applyTo(new Vector3D(1, craftVectors.getRadius()));
    Vector3D momentum = position.crossProduct(velocity);
    Vector3D eccentricity = getEccentricity(velocity, momentum, body, position);
    Vector3D ascendingNodeVector = Vector3D.PLUS_K.crossProduct(momentum);
    double semiMajorAxis = getSemiMajorAxis(momentum, body, eccentricity);
    double inclination = getInclination(momentum);
    double rightAscension = getRightAscension(ascendingNodeVector);
    double argumentPE = getArgumentPE(ascendingNodeVector, eccentricity);
    double trueAnomaly = getTrueAnomaly(eccentricity, position, velocity);
    double eccentricAnomaly = getEccentricAnomaly(eccentricity, trueAnomaly);
    double meanAnomaly = getMeanAnomaly(eccentricAnomaly, eccentricity);
    vectors =
        OrbitalVectors.builder()
            .centralBody(body)
            .velocity(velocity)
            .position(position)
            .momentum(momentum)
            .eccentricity(eccentricity)
            .semiMajorAxis(semiMajorAxis)
            .inclination(inclination)
            .rightAscension(rightAscension)
            .argumentPE(argumentPE)
            .meanAnomaly(meanAnomaly)
            .build();
    return this;
  }

  public Orbit getAsOrbit(){
    return new OrbitBuilder().buildFromVectors(vectors).getOrbit();
  }

  private double getMeanAnomaly(double eccentricAnomaly, Vector3D eccentricityVector) {
    var eccentricity = eccentricityVector.getNorm();
    if (eccentricity >= 1) {
      return 0;
    }
    return eccentricAnomaly - (eccentricAnomaly * sin(eccentricAnomaly));
  }

  private double getEccentricAnomaly(Vector3D eccentricityVector, double trueAnomaly) {
    var eccentricity = eccentricityVector.getNorm();
    if (eccentricity >= 1) {
      return 0;
    }
    var sinE = Math.sqrt(1 - Math.pow(eccentricity, 2)) * Math.sin(trueAnomaly);
    var cosE = eccentricity + Math.cos(trueAnomaly);
    return atan2(sinE, cosE) % 2 * Math.PI;
  }

  private double getTrueAnomaly(Vector3D eccentricity, Vector3D position, Vector3D velocity) {
    var cosAnomaly =
        eccentricity.dotProduct(position) / (eccentricity.getNorm()) * position.getNorm();
    var anomaly = Math.acos(cosAnomaly);
    var quadrantCheck = position.dotProduct(velocity);
    return quadrantCheck >= 0 ? anomaly : 2 * Math.PI - anomaly;
  }

  private double getArgumentPE(Vector3D ascendingNodeVector, Vector3D eccentricity) {
    var cosOmega =
        ascendingNodeVector.dotProduct(eccentricity)
            / (ascendingNodeVector.getNorm() * eccentricity.getNorm());
    var omega = Math.acos(cosOmega);
    return eccentricity.getZ() >= 0 ? omega : 2 * Math.PI - omega;
  }

  private double getRightAscension(Vector3D ascendingNodeVector) {
    var nodeI = Vector3D.PLUS_I.dotProduct(ascendingNodeVector) / ascendingNodeVector.getNorm();
    var omega = Math.acos(nodeI);
    var nodeJ = Vector3D.PLUS_J.dotProduct(ascendingNodeVector) / ascendingNodeVector.getNorm();
    return nodeJ >= 0 ? omega : 2 * Math.PI - omega;
  }

  private double getInclination(Vector3D momentum) {
    var cosI = Vector3D.PLUS_K.dotProduct(momentum) / momentum.getNorm();
    return Math.acos(cosI);
  }

  private double getSemiMajorAxis(Vector3D momentumVector, Body body, Vector3D eccentricityVector) {
    double eccentricity = eccentricityVector.getNorm();
    if (eccentricity == 1) {
      return Double.MAX_VALUE;
    }
    double eccentricitySquared = Math.pow(eccentricity, 2);
    double momentumSquared = Math.pow(momentumVector.getNorm(), 2);
    double semiLatusRectum = momentumSquared / body.getMu();
    return semiLatusRectum / (1 - eccentricitySquared);
  }

  private Vector3D getEccentricity(
      Vector3D velocity, Vector3D momentum, Body body, Vector3D position) {
    var vector1 = velocity.crossProduct(momentum).scalarMultiply(1 / body.getMu());
    var vector2 = position.scalarMultiply(1 / position.getNorm());
    return vector1.subtract(vector2);
  }
}
