package org.artools.orbitcalculator.method;

import java.sql.Timestamp;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.AstralPosition;
import org.artools.orbitcalculator.application.OrbitInfo;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.method.vector.OrbitInfoWriter;
import org.artools.orbitcalculator.method.vector.OrbitalStateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AstralStateMapper {
  @Named("orreryToAstralState")
  default AstralPosition orreryToAstralState(AstralBody body) {
    String name = body.getName();
    MotionState motionState = body.getMotionState();
    double radius = body instanceof Planet planet ? planet.getBodyRadius() : 1.0;

    AstralPosition.AstralPositionBuilder builder =
        AstralPosition.builder()
            .body(name)
            .radius(radius)
            .timestamp(Timestamp.from(motionState.getEpoch()));

    if (!(motionState instanceof OrbitalState state)) {
      return builder
          .positionX(motionState.getPosition().getX())
          .positionY(motionState.getPosition().getY())
          .positionZ(motionState.getPosition().getZ())
          .velocityX(motionState.getVelocity().getX())
          .velocityY(motionState.getVelocity().getY())
          .velocityZ(motionState.getVelocity().getZ())
          .build();
    }

    OrbitalStateUtils utils = new OrbitalStateUtils();
    Vector3D truePosition = utils.getTruePosition(state);
    Vector3D trueVelocity = utils.getTrueVelocity(state);
    OrbitInfo orbit = new OrbitInfoWriter(state).getOrbitInfo();
    return builder
        .positionX(truePosition.getX())
        .positionY(truePosition.getY())
        .positionZ(truePosition.getZ())
        .velocityX(trueVelocity.getX())
        .velocityY(trueVelocity.getY())
        .velocityZ(trueVelocity.getZ())
        .orbit(orbit)
        .build();
  }
}
