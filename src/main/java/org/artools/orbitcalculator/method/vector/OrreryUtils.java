package org.artools.orbitcalculator.method.vector;

import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.artools.orbitcalculator.application.bodies.AstralBodies;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;

@Getter
public class OrreryUtils {
  protected final Orrery orrery;

  public OrreryUtils(Orrery orrery) {
    this.orrery = orrery;
  }

  public OrreryUtils() {
    this.orrery = new Orrery();
  }

  public Orrery convertToOrbitalStates() {
    orrery.getBodyStateMap().forEach(this::convertToOrbitalState);
    return orrery;
  }

  private void convertToOrbitalState(AstralBodies astralBodies, MotionState satelliteState) {
    Optional<AstralBodies> centralBodyOptional = findOrbitalFocus(astralBodies);
    if (centralBodyOptional.isEmpty()) {
      return;
    }
    AstralBodies centralAstralBodies = centralBodyOptional.get();
    MotionState centralBodyState = orrery.getMotionVectors(centralAstralBodies);
    OrbitalState state =
        new OrbitalStateBuilder(satelliteState, centralBodyState, centralAstralBodies).getVectors();
    orrery.setMotionState(astralBodies, state);
  }

  private Optional<AstralBodies> findOrbitalFocus(AstralBodies satellite) {
    if (!satellite.equals(AstralBodies.CRAFT)) {
      return Optional.ofNullable(satellite.getOrbitalFocus());
    }
    return maximumAccelerationValue(satellite);
  }

  private Optional<AstralBodies> maximumAccelerationValue(AstralBodies satellite) {
    MotionState satelliteState = orrery.getMotionVectors(satellite);

    return orrery.getBodyStateMap().entrySet().stream()
        .filter(entry -> !entry.getKey().equals(satellite))
        .map(entry -> findAccelerationTowards(satelliteState, entry.getKey(), entry.getValue()))
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
  }

  private Map.Entry<AstralBodies, Double> findAccelerationTowards(
      MotionState satelliteState, AstralBodies focusAstralBodies, MotionState focusBodyState) {
    double focusBodyMu = focusAstralBodies.getMu();
    double distance = focusBodyState.getPosition().subtract(satelliteState.getPosition()).getNorm();
    double acceleration = focusBodyMu / Math.pow(distance, 2);
    return Map.entry(focusAstralBodies, acceleration);
  }
}
