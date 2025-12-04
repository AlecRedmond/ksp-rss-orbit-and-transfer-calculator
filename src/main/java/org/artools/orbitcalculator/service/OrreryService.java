package org.artools.orbitcalculator.service;

import java.sql.Timestamp;
import java.time.Instant;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.jpa.CraftDTO;
import org.artools.orbitcalculator.application.kepler.KeplerHolds;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.CraftNotFoundException;
import org.artools.orbitcalculator.method.integrator.OrreryEpochController;
import org.artools.orbitcalculator.method.integrator.OrreryIntegrator;
import org.artools.orbitcalculator.method.jpa.SpaceCraftMapper;
import org.artools.orbitcalculator.method.kepler.KeplerBuilder;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrreryService {
  private final AstralPositionService positionService;
  private final OrbitService orbitService;
  private final KeplerHoldsService keplerHoldsService;
  private final CraftService craftService;
  private final SpaceCraftMapper spaceCraftMapper;
  private Orrery orrery;
  private OrreryEpochController orreryEpochController;

  @Autowired
  public OrreryService(
      AstralPositionService positionService,
      OrbitService orbitService,
      KeplerHoldsService keplerHoldsService,
      CraftService craftService,
      SpaceCraftMapper spaceCraftMapper) {
    this.positionService = positionService;
    this.orbitService = orbitService;
    this.keplerHoldsService = keplerHoldsService;
    this.craftService = craftService;
    this.spaceCraftMapper = spaceCraftMapper;
    this.orrery = new OrreryBuilder().getOrrery();
    this.orreryEpochController = new OrreryEpochController(orrery);
  }

  @Transactional
  public KeplerOrbit buildOrbit(String keplerOrbitId, String keplerHoldId) {
    KeplerOrbit orbit = orbitService.getOrbitStateByID(keplerOrbitId).orElseThrow();
    KeplerHolds holds = keplerHoldsService.findById(keplerHoldId).orElseThrow();
    Planet centralBody = orrery.getPlanetByType(orbit.getCentralBodyType());
    orbit = new KeplerBuilder(orbit, centralBody, holds).getOrbit();
    return orbitService.updateOrbitState(orbit, keplerOrbitId);
  }

  @Transactional
  public CraftDTO addCraftToOrrery(String craftId) {
    // TODO - spacecraft repository, convert to craft, add craft with ID to orrery
    CraftDTO craftDTO =
        craftService.findById(craftId).orElseThrow(() -> new CraftNotFoundException(craftId));
    Craft craft = spaceCraftMapper.dtoToCraft(craftDTO, orrery);

    return craftDTO;
  }

  public void setOrreryEpoch(Timestamp timestamp) {
    Instant epoch = timestamp.toInstant();
    if (epoch.equals(orrery.getEpoch())) {
      return;
    }
    orrery = new OrreryIntegrator(orrery).stepToTime(epoch).getOrrery();
    positionService.saveOrreryState(orrery);
  }
}
