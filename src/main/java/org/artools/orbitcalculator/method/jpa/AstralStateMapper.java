package org.artools.orbitcalculator.method.jpa;

import java.sql.Timestamp;
import org.artools.orbitcalculator.application.jpa.AstralStateDTO;
import org.artools.orbitcalculator.application.jpa.AstralStateDTO.AstralStateDTOBuilder;
import org.artools.orbitcalculator.application.jpa.Vector3DTO;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.method.kepler.KeplerBuilder;
import org.artools.orbitcalculator.method.vector.MotionStateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AstralStateMapper {


  @Named("motionStateToDto")
  default AstralStateDTO motionStateToDto(MotionState motionState) {
    AstralStateDTOBuilder builder =
        AstralStateDTO.builder().timestamp(Timestamp.from(motionState.getEpoch()));

    if (!(motionState instanceof OrbitalState state)) {
      Vector3DTO position = new Vector3DTO(motionState.getPosition());
      Vector3DTO velocity = new Vector3DTO(motionState.getVelocity());
      return builder.position(position).velocity(velocity).build();
    }

    MotionStateUtils utils = new MotionStateUtils();
    Vector3DTO truePosition = new Vector3DTO(utils.getTruePosition(state));
    Vector3DTO trueVelocity = new Vector3DTO(utils.getTrueVelocity(state));
    KeplerOrbit orbit = new KeplerBuilder(state).getOrbit();
    return builder.position(truePosition).velocity(trueVelocity).orbit(orbit).build();
  }
}
