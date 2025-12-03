package org.artools.orbitcalculator.method.jpa;

import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.jpa.SpaceCraftDTO;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.vector.OrbitStateBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SpaceCraftMapper {
  @Named("dtoToCraft")
  default Craft dtoToCraft(SpaceCraftDTO dto, KeplerOrbit orbit, Orrery orrery) {
    OrbitalState state = OrbitStateBuilder.buildFromKeplerOrbit(orbit, orrery);

  }

  @Named("craftToDto")
  default SpaceCraftDTO craftToDto(Craft craft,AstralStateMapper astralStateMapper) {
      return SpaceCraftDTO.builder()
              .id(craft.getId())
              .bodyRadius(craft.getBodyRadius())
              .engineIsp(craft.getEngineISP())
              .currentMass(craft.getMass())
              .dryMass(craft.getDryMass())
              .remainingDeltaV(calculateRemainingDeltaV(craft))
              .astralPositionDTO(astralStateMapper.orreryToAstralState(craft))
              .build();
  }

   default double calculateRemainingDeltaV(Craft craft){

   }
}
