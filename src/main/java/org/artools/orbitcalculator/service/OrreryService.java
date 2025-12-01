package org.artools.orbitcalculator.service;

import java.sql.Timestamp;
import java.time.Instant;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.jpa.SpaceCraftDTO;
import org.artools.orbitcalculator.application.kepler.KeplerHolds;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.integrator.OrreryIntegrator;
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
  private Orrery orrery;

  @Autowired
  public OrreryService(
      AstralPositionService positionService,
      OrbitService orbitService,
      KeplerHoldsService keplerHoldsService) {
    this.positionService = positionService;
    this.orbitService = orbitService;
    this.keplerHoldsService = keplerHoldsService;
    this.orrery = new OrreryBuilder().getOrrery();
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
  public SpaceCraftDTO addCraftToOrrery(SpaceCraftDTO spaceCraftDTO, KeplerOrbit orbit) {
    setEpoch(orbit.getTimestamp());
    spaceCraftDTO.setCurrentOrbit(orbit);
    //TODO - spacecraft repository, convert to craft, add craft with ID to orrery
    return spaceCraftDTO;
  }

  public void setEpoch(Timestamp timestamp) {
    Instant epoch = timestamp.toInstant();
    if (epoch.equals(orrery.getEpoch())) {
      return;
    }
    orrery = new OrreryIntegrator(orrery).stepToTime(epoch).getOrrery();
    positionService.saveOrreryState(orrery);
  }
}
