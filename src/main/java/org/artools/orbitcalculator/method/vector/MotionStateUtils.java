package org.artools.orbitcalculator.method.vector;

import java.time.Instant;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.PerifocalState;

@NoArgsConstructor
public class MotionStateUtils {

  public static MotionState copyOf(MotionState motionState) {
    if (motionState instanceof OrbitalState orbitalState) {
      return OrbitalState.builder()
          .velocity(orbitalState.getVelocity().scalarMultiply(1))
          .position(orbitalState.getPosition().scalarMultiply(1))
          .epoch(Instant.ofEpochSecond(orbitalState.getEpoch().getEpochSecond()))
          .mass(orbitalState.getMass())
          .momentum(orbitalState.getMomentum().scalarMultiply(1))
          .eccentricity(orbitalState.getEccentricity().scalarMultiply(1))
          .semiMajorAxis(orbitalState.getSemiMajorAxis())
          .longitudeAscendingNode(orbitalState.getLongitudeAscendingNode())
          .inclination(orbitalState.getInclination())
          .argumentPE(orbitalState.getArgumentPE())
          .trueAnomaly(orbitalState.getTrueAnomaly())
          .eccentricAnomaly(orbitalState.getEccentricAnomaly())
          .meanAnomaly(orbitalState.getMeanAnomaly())
          .apoapsisAltitude(orbitalState.getApoapsisAltitude())
          .periapsisAltitude(orbitalState.getPeriapsisAltitude())
          .build();
    }
    return MotionState.builder()
        .velocity(motionState.getVelocity().scalarMultiply(1))
        .position(motionState.getPosition().scalarMultiply(1))
        .epoch(Instant.ofEpochSecond(motionState.getEpoch().getEpochSecond()))
        .mass(motionState.getMass())
        .build();
  }

  public PerifocalState convertToPerifocal(OrbitalState orbitalState) {
    if (orbitalState instanceof PerifocalState state) {
      return state;
    }
    double argumentPE = orbitalState.getArgumentPE();
    double inclination = orbitalState.getInclination();
    double rightAscension = orbitalState.getLongitudeAscendingNode();
    Rotation rotation =
        new Rotation(
            RotationOrder.ZXZ,
            RotationConvention.FRAME_TRANSFORM,
            -argumentPE,
            -inclination,
            -rightAscension);

    Vector3D position = rotation.applyTo(orbitalState.getPosition());
    Vector3D velocity = rotation.applyTo(orbitalState.getVelocity());
    Vector3D eccentricity = rotation.applyTo(orbitalState.getEccentricity());
    Vector3D momentum = rotation.applyTo(orbitalState.getMomentum());

    return PerifocalState.builder()
        .centralBody(orbitalState.getCentralBody())
        .position(position)
        .velocity(velocity)
        .momentum(momentum)
        .eccentricity(eccentricity)
        .semiMajorAxis(orbitalState.getSemiMajorAxis())
        .longitudeAscendingNode(rightAscension)
        .inclination(inclination)
        .argumentPE(argumentPE)
        .trueAnomaly(orbitalState.getTrueAnomaly())
        .eccentricAnomaly(orbitalState.getEccentricAnomaly())
        .meanAnomaly(orbitalState.getMeanAnomaly())
        .epoch(orbitalState.getEpoch())
        .build();
  }

  public OrbitalState convertFromPerifocal(PerifocalState perifocalState) {
    double argumentPE = perifocalState.getArgumentPE();
    double inclination = perifocalState.getInclination();
    double rightAscension = perifocalState.getLongitudeAscendingNode();
    Rotation rotation =
        new Rotation(
            RotationOrder.ZXZ,
            RotationConvention.FRAME_TRANSFORM,
            rightAscension,
            inclination,
            argumentPE);

    Vector3D position = rotation.applyTo(perifocalState.getPosition());
    Vector3D velocity = rotation.applyTo(perifocalState.getVelocity());
    Vector3D eccentricity = rotation.applyTo(perifocalState.getEccentricity());
    Vector3D momentum = rotation.applyTo(perifocalState.getMomentum());

    return OrbitalState.builder()
        .centralBody(perifocalState.getCentralBody())
        .position(position)
        .velocity(velocity)
        .momentum(momentum)
        .eccentricity(eccentricity)
        .semiMajorAxis(perifocalState.getSemiMajorAxis())
        .longitudeAscendingNode(rightAscension)
        .inclination(inclination)
        .argumentPE(argumentPE)
        .trueAnomaly(perifocalState.getTrueAnomaly())
        .eccentricAnomaly(perifocalState.getEccentricAnomaly())
        .meanAnomaly(perifocalState.getMeanAnomaly())
        .epoch(perifocalState.getEpoch())
        .build();
  }

  public Vector3D getTruePosition(OrbitalState state) {
    if (state instanceof PerifocalState perifocalState) {
      return getTruePosition(convertFromPerifocal(perifocalState));
    }

    Vector3D position = new Vector3D(1, state.getPosition());
    MotionState parentState = state.getCentralBody().getCurrentMotionState();
    Vector3D parentPosition =
        parentState instanceof OrbitalState subState
            ? getTruePosition(subState)
            : new Vector3D(1, parentState.getPosition());
    return position.add(parentPosition);
  }

  public Vector3D getTrueVelocity(OrbitalState state) {
    if (state instanceof PerifocalState perifocalState) {
      return getTrueVelocity(convertFromPerifocal(perifocalState));
    }
    Vector3D velocity = new Vector3D(1, state.getVelocity());
    MotionState parentState = state.getCentralBody().getCurrentMotionState();
    Vector3D parentVelocity =
        parentState instanceof OrbitalState subState
            ? getTruePosition(subState)
            : new Vector3D(1, parentState.getVelocity());
    return velocity.add(parentVelocity);
  }
}
