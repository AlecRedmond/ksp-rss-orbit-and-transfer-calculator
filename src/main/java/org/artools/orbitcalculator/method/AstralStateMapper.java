package org.artools.orbitcalculator.method;

import java.sql.Timestamp;
import java.util.Map;
import org.artools.orbitcalculator.application.AstralPosition;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AstralStateMapper {
  @Named("orreryToAstralState")
  default AstralPosition orreryToAstralState(AstralBody body) {
    BodyType bodyType = body.getBodyType();
    MotionState motionState = body.getMotionState();
    return AstralPosition.builder()
        .body(bodyType.name())
        .timestamp(Timestamp.from(motionState.getEpoch()))
        .xpos(motionState.getPosition().getX())
        .ypos(motionState.getPosition().getY())
        .zpos(motionState.getPosition().getZ())
        .build();
  }
}
