package org.artools.orbitcalculator.method.jpa;

import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.jpa.CraftDTO;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.NotOrbitalStateException;
import org.artools.orbitcalculator.method.kepler.KeplerBuilder;
import org.artools.orbitcalculator.method.vector.OrbitStateBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SpaceCraftMapper {
  @Named("dtoToCraft")
  default Craft dtoToCraft(CraftDTO craftDTO, Orrery orrery) {
    OrbitalState state = OrbitStateBuilder.buildFromKeplerOrbit(craftDTO.getOrbit(), orrery);
    return new Craft(
        state,
        craftDTO.getId(),
        craftDTO.getBodyRadius(),
        craftDTO.getEngineIsp(),
        craftDTO.getEngineThrustNewtons(),
        craftDTO.getCurrentMass(),
        craftDTO.getDryMass());
  }

  @Named("craftToDto")
  default CraftDTO craftToDto(Craft craft) {
    if (!(craft.getCurrentMotionState() instanceof OrbitalState orbitalState)) {
      throw new NotOrbitalStateException(craft);
    }

    KeplerOrbit orbit = new KeplerBuilder(orbitalState).getOrbit();

    return CraftDTO.builder()
        .id(craft.getId())
        .bodyRadius(craft.getBodyRadius())
        .engineIsp(craft.getEngineISP())
        .currentMass(craft.getMass())
        .dryMass(craft.getDryMass())
        .remainingDeltaV(craft.getRemainingDeltaV())
        .orbit(orbit)
        .build();
  }
}
