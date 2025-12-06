package org.artools.orbitcalculator.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.jpa.CraftDTO;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.integrator.OrreryIntegrator;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OrreryEpochController {
  private static final Duration INTERMEDIATE_DURATION = Duration.of(10, ChronoUnit.MINUTES);
  @Getter private final Orrery orrery;
  private final AstralPositionService positionService;
  private final OrbitService orbitService;
  private final KeplerHoldsService keplerHoldsService;
  private final CraftService craftService;
  private final OrreryIntegrator integrator;
  private List<CraftDTO> crafts;

  @Autowired
  public OrreryEpochController(
      Orrery orrery,
      AstralPositionService positionService,
      OrbitService orbitService,
      KeplerHoldsService keplerHoldsService,
      CraftService craftService) {
    this.orrery = orrery;
    this.positionService = positionService;
    this.orbitService = orbitService;
    this.keplerHoldsService = keplerHoldsService;
    this.craftService = craftService;
    this.crafts = new ArrayList<>();
    this.integrator = new OrreryIntegrator(orrery);
  }

  /*

  public void setCrafts(List<Craft> crafts) {
    this.crafts = sortCraftsAndValidateInitializationTime(craftService.fetchAll());
    Instant earliestCraftInit = this.crafts.getFirst().getInitializationTime();
    boolean rollbackRequired = earliestCraftInit.isBefore(orrery.getEpoch());
    if (rollbackRequired) {
      rollBackOrrery(earliestCraftInit);
    }
  }

  public void stepToTime(Instant desiredTime) {
    if (desiredTime.isBefore(orrery.getEpoch())) {
      rollBackOrrery(desiredTime);
    }
    List<Craft> toAdd = getCraftsToAdd(orrery.getEpoch(), desiredTime);
    Duration duration;
    for (Craft craft : toAdd) {
      Instant nextTimeStep = craft.getInitializationTime();
      duration = Duration.between(orrery.getEpoch(), nextTimeStep);
      integrateForwardBy(duration);
      orrery.getAstralBodies().add(craft);
      integrator.rebuildStateVector();
    }
    duration = Duration.between(orrery.getEpoch(), desiredTime);
    integrateForwardBy(duration);
  }

  private void integrateForwardBy(Duration duration) {
    if (duration.isZero()) return;
    try {
      integrator.stepByDuration(duration);
      positionService.saveOrreryState(orrery);
    } catch (NumberIsTooSmallException e) {
      log.warn(
          "Duration %s was too small for the forward integrator to handle! Using intermediate step..."
              .formatted(duration.toString()));
      integrator.stepByDuration(INTERMEDIATE_DURATION);
      Duration reverseDuration = duration.minus(INTERMEDIATE_DURATION);
      integrator.stepByDuration(reverseDuration);
      positionService.saveOrreryState(orrery);
    }
  }

  private List<Craft> getCraftsToAdd(Instant currentEpoch, Instant desiredTime) {
    return crafts.stream()
        .filter(craft -> !orrery.getAstralBodies().contains(craft))
        .filter(craft -> !craft.getInitializationTime().isBefore(currentEpoch))
        .filter(craft -> !craft.getInitializationTime().isAfter(desiredTime))
        .sorted(Comparator.comparing(Craft::getInitializationTime))
        .toList();
  }

  private List<CraftDTO> sortCraftsAndValidateInitializationTime(List<CraftDTO> crafts) {
    List<CraftDTO> newCraftDTOs =
        crafts.stream()
            .filter(cdto -> cdto.getInitialOrbit().isAllElementsBuilt())
            .sorted(Comparator.comparing(cdto -> cdto.getInitialOrbit().getTimestamp()))
            .filter(
                craft ->
                    craft
                        .getInitialOrbit()
                        .getTimestamp()
                        .toInstant()
                        .isBefore(Planet.PLANET_INITIALIZATION))
            .toList();
    boolean anyCraftsInitBeforePlanets = crafts.size() != newCraftDTOs.size();
    if (anyCraftsInitBeforePlanets) {
      throw new IllegalArgumentException("Attempted to add a craft initialised before planets");
    }
    return newCraftDTOs;
  }

  private Timestamp craftOrbitInitTime()

  private void rollBackOrrery(Instant targetRollbackTime) {
    Map<AstralBody, Instant> rollBackTimes = new HashMap<>();
    positionService.deletePositionsAfterTime(Timestamp.from(targetRollbackTime));
    for (AstralBody body : orrery.getAstralBodies()) {
      Instant rollBackTime = body.rollBackStates(targetRollbackTime);
      rollBackTimes.put(body, rollBackTime);
    }
    Instant earliestRollBack =
        rollBackTimes.values().stream().min(Comparator.naturalOrder()).orElseThrow();
    checkPlanetEpochsAreEqual(rollBackTimes);
    removeCraftInitializedAfter(earliestRollBack, rollBackTimes);
    integrator.rebuildStateVector();
    stepToTime(targetRollbackTime);
    positionService.saveOrreryState(orrery);
  }

  private void checkPlanetEpochsAreEqual(Map<AstralBody, Instant> rollBackTimes) {
    Set<Instant> planetEpochs =
        rollBackTimes.entrySet().stream()
            .filter(entry -> entry.getKey() instanceof Planet)
            .map(Map.Entry::getValue)
            .collect(Collectors.toSet());

    if (planetEpochs.size() != 1) {
      String epochStrings =
          planetEpochs.stream()
              .map(Instant::toString)
              .map(s -> s + ", ")
              .collect(Collectors.joining());
      throw new RuntimeException(
          "Not all planet epochs are equal! Got epochs %s".formatted(epochStrings));
    }
  }

  private void removeCraftInitializedAfter(
      Instant earliestRollBack, Map<AstralBody, Instant> rollBackTimes) {
    rollBackTimes.keySet().stream()
        .filter(Craft.class::isInstance)
        .map(Craft.class::cast)
        .filter(c -> c.getInitializationTime().isAfter(earliestRollBack))
        .forEach(craft -> orrery.getAstralBodies().remove(craft));
  }


   */
}
