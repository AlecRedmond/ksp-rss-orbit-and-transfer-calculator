package org.artools.orbitcalculator.method;

import java.sql.Timestamp;
import java.util.Map;
import org.artools.orbitcalculator.application.AstralState;
import org.artools.orbitcalculator.application.bodies.AstralBodies;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AstralStateMapper {
  @Named("orreryToAstralState")
  default AstralState orreryToAstralState(Map.Entry<AstralBodies, MotionState> entry) {
    AstralBodies astralBodies = entry.getKey();
    MotionState motionState = entry.getValue();
    return AstralState.builder()
        .body(astralBodies.name())
        .timestamp(Timestamp.from(motionState.getEpoch()))
        .xPos(motionState.getPosition().getX())
        .yPos(motionState.getPosition().getY())
        .zPos(motionState.getPosition().getZ())
        .build();
  }
}
