package org.artools.orbitcalculator.method.jpa;

import java.sql.Timestamp;
import java.util.List;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.jpa.*;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.kepler.KeplerBuilder;
import org.artools.orbitcalculator.method.vector.MotionStateUtils;
import org.artools.orbitcalculator.method.vector.OrbitStateBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AstralBodyMapper {
  @Named("dtoToCraft")
  default Craft dtoToCraft(CraftDTO craftDTO, Orrery orrery) {
    OrbitalState state = OrbitStateBuilder.buildFromKeplerOrbit(craftDTO.getInitialOrbit(), orrery);
    return new Craft(
        state,
        craftDTO.getId(),
        craftDTO.getBodyRadius(),
        craftDTO.getEngineIsp(),
        craftDTO.getEngineThrustNewtons(),
        craftDTO.getMass(),
        craftDTO.getDryMass(),
        craftDTO.getInitialOrbit(),
        craftDTO.getFinalOrbit());
  }

  @Named("bodyToDto")
  default AstralBodyDTO bodyToDto(AstralBody body) {
    switch (body) {
      case Craft craft -> {
        return craftToDto(craft);
      }
      case Planet planet -> {
        return planetToDto(planet);
      }
      default -> throw new IllegalStateException("Unexpected value: " + body);
    }
  }

  @Named("craftToDto")
  default CraftDTO craftToDto(Craft craft) {
    List<AstralStateDTO> snapshots =
        craft.getSnapshots().stream().map(this::motionStateToDto).toList();

    return CraftDTO.builder()
        .id(craft.getId())
        .bodyRadius(craft.getBodyRadius())
        .engineIsp(craft.getEngineISP())
        .mass(craft.getMass())
        .mu(craft.getMu())
        .snapshots(snapshots)
        .dryMass(craft.getDryMass())
        .remainingDeltaV(craft.getRemainingDeltaV())
        .initialOrbit(craft.getInitialOrbit())
        .finalOrbit(craft.getFinalOrbit())
        .build();
  }

  @Named("planetToDto")
  default PlanetDTO planetToDto(Planet planet) {
    List<AstralStateDTO> snapshots =
        planet.getSnapshots().stream().map(this::motionStateToDto).toList();

    return PlanetDTO.builder()
        .bodyRadius(planet.getBodyRadius())
        .mass(planet.getMass())
        .mu(planet.getMu())
        .snapshots(snapshots)
        .bodyType(planet.getBodyType())
        .build();
  }

  @Named("motionStateToDto")
  default AstralStateDTO motionStateToDto(MotionState motionState) {
    AstralStateDTO.AstralStateDTOBuilder builder =
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
