package org.artools.orbitcalculator.service;

import java.time.Instant;

import org.artools.orbitcalculator.application.jpa.OrbitStateDTO;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.integrator.OrreryIntegrator;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrreryService {
  @Autowired private final AstralPositionService positionService;
  @Autowired private final OrbitService orbitService;
  private Orrery orrery;

  public OrreryService(AstralPositionService positionService, OrbitService orbitService) {
    this.positionService = positionService;
    this.orbitService = orbitService;
    this.orrery = new OrreryBuilder().getOrrery();
  }

  public void setEpoch(Instant epoch) {
    if (epoch.equals(orrery.getEpoch())) {
      return;
    }
    orrery = new OrreryIntegrator(orrery).stepToTime(epoch).getOrrery();
    positionService.saveOrreryState(orrery);
  }

  public void addAstralBody(OrbitStateDTO orbitStateDTO){

  }
}
