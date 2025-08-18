package org.artools.orbitcalculator.method;

import java.sql.Timestamp;
import org.artools.orbitcalculator.application.OrbitInfo;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.entity.AstralPositionDTO;
import org.artools.orbitcalculator.application.vector.entity.Vector3;
import org.artools.orbitcalculator.method.vector.OrbitInfoWriter;
import org.artools.orbitcalculator.method.vector.OrbitalStateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AstralStateMapper {
  @Named("orreryToAstralState")
  default AstralPositionDTO orreryToAstralState(AstralBody body) {
    String name = body.getNameString();
    MotionState motionState = body.getMotionState();
    double radius = body instanceof Planet planet ? planet.getBodyRadius() : 1.0;

    AstralPositionDTO.AstralPositionDTOBuilder builder =
        AstralPositionDTO.builder()
            .body(name)
            .radius(radius)
            .timestamp(Timestamp.from(motionState.getEpoch()));

    if (!(motionState instanceof OrbitalState state)) {
      Vector3 position = new Vector3(motionState.getPosition());
      Vector3 velocity = new Vector3(motionState.getVelocity());
      return builder.position(position).velocity(velocity).build();
    }

    OrbitalStateUtils utils = new OrbitalStateUtils();
    Vector3 truePosition = new Vector3(utils.getTruePosition(state));
    Vector3 trueVelocity = new Vector3(utils.getTrueVelocity(state));
    OrbitInfo orbit = new OrbitInfoWriter(state).getOrbitInfo();
    return builder.position(truePosition).velocity(trueVelocity).orbit(orbit).build();
  }
}
