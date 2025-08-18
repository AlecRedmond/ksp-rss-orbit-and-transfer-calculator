package org.artools.orbitcalculator.method.jpa;

import java.sql.Timestamp;
import org.artools.orbitcalculator.application.jpa.OrbitDTO;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.jpa.AstralPositionDTO;
import org.artools.orbitcalculator.application.jpa.Vector3DTO;
import org.artools.orbitcalculator.method.vector.OrbitalStateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AstralStateMapper {
  @Named("orreryToAstralState")
  default AstralPositionDTO orreryToAstralState(AstralBody body) {
    String name = body.getName();
    MotionState motionState = body.getMotionState();
    double radius = body instanceof Planet planet ? planet.getBodyRadius() : 1.0;

    AstralPositionDTO.AstralPositionDTOBuilder builder =
        AstralPositionDTO.builder()
            .body(name)
            .radius(radius)
            .timestamp(Timestamp.from(motionState.getEpoch()));

    if (!(motionState instanceof OrbitalState state)) {
      Vector3DTO position = new Vector3DTO(motionState.getPosition());
      Vector3DTO velocity = new Vector3DTO(motionState.getVelocity());
      return builder.position(position).velocity(velocity).build();
    }

    OrbitalStateUtils utils = new OrbitalStateUtils();
    Vector3DTO truePosition = new Vector3DTO(utils.getTruePosition(state));
    Vector3DTO trueVelocity = new Vector3DTO(utils.getTrueVelocity(state));
    OrbitDTO orbit = new OrbitDTOWriter(state).getOrbitDTO();
    return builder.position(truePosition).velocity(trueVelocity).orbit(orbit).build();
  }
}
