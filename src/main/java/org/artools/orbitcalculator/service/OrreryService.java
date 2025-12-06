package org.artools.orbitcalculator.service;

import java.sql.Timestamp;
import java.util.List;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.jpa.AstralStateDTO;
import org.artools.orbitcalculator.application.kepler.KeplerHolds;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.Orrery;
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
  private final OrreryEpochController orreryEpochController;
  private final Orrery orrery;

  @Autowired
  public OrreryService(
      AstralPositionService positionService,
      OrbitService orbitService,
      KeplerHoldsService keplerHoldsService,
      CraftService craftService) {
    this.positionService = positionService;
    this.orbitService = orbitService;
    this.keplerHoldsService = keplerHoldsService;
    this.craftService = craftService;
    this.orrery = new OrreryBuilder().getOrrery();
    this.orreryEpochController =
        new OrreryEpochController(
            orrery, positionService, orbitService, keplerHoldsService, craftService);
  }

  @Transactional
  public KeplerOrbit buildOrbit(String keplerOrbitId, String keplerHoldId) {
    KeplerOrbit orbit = orbitService.getOrbitStateByID(keplerOrbitId).orElseThrow();
    KeplerHolds holds = keplerHoldsService.findById(keplerHoldId).orElseThrow();
    Planet centralBody = orrery.getPlanetByType(orbit.getCentralBodyType());
    orbit = new KeplerBuilder(orbit, centralBody, holds).getOrbit();
    return orbitService.updateOrbitState(orbit, keplerOrbitId);
  }

  public void addAllCraftsToOrrery() {
    //    List<Craft> craftList =
    //        craftService.fetchAll().stream()
    //            .map(craftDTO -> craftService.dtoToCraft(craftDTO, orrery))
    //            .toList();
    //
    //    orreryEpochController.setCrafts(craftList);
  }

  @Transactional
  public List<AstralStateDTO> setOrreryEpoch(Timestamp timestamp) {
    //    orreryEpochController.stepToTime(timestamp.toInstant());
    //    return positionService.saveOrreryState(orrery);
    return null;
  }
}
