package org.artools.orbitcalculator.method.vector;

import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.artools.orbitcalculator.application.bodies.Body;
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

  private void convertToOrbitalState(Body body, MotionState satelliteState) {
    Optional<Body> centralBodyOptional = findOrbitalFocus(body);
    if (centralBodyOptional.isEmpty()) {
      return;
    }
    Body centralBody = centralBodyOptional.get();
    MotionState centralBodyState = orrery.getMotionVectors(centralBody);
    OrbitalState state =
        new OrbitalStateBuilder(satelliteState, centralBodyState, centralBody).getVectors();
    orrery.setMotionState(body, state);
  }

  private Optional<Body> findOrbitalFocus(Body satellite) {
    if (!satellite.equals(Body.CRAFT)) {
      return Optional.ofNullable(satellite.getOrbitalFocus());
    }
    return maximumAccelerationValue(satellite);
  }

  private Optional<Body> maximumAccelerationValue(Body satellite) {
    MotionState satelliteState = orrery.getMotionVectors(satellite);

    return orrery.getBodyStateMap().entrySet().stream()
        .filter(entry -> !entry.getKey().equals(satellite))
        .map(entry -> findAccelerationTowards(satelliteState, entry.getKey(), entry.getValue()))
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
  }

  private Map.Entry<Body, Double> findAccelerationTowards(
      MotionState satelliteState, Body focusBody, MotionState focusBodyState) {
    double focusBodyMu = focusBody.getMu();
    double distance = focusBodyState.getPosition().subtract(satelliteState.getPosition()).getNorm();
    double acceleration = focusBodyMu / Math.pow(distance, 2);
    return Map.entry(focusBody, acceleration);
  }
}
