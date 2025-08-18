package org.artools.orbitcalculator.method.vector;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;

@Getter
public class OrreryUtils {
  protected final Orrery orrery;

  public OrreryUtils(Orrery orrery) {
    this.orrery = orrery;
  }

  public Orrery convertToOrbitalStates() {
    orrery.getAstralBodies().forEach(this::convertToOrbitalState);
    return orrery;
  }

  private void convertToOrbitalState(AstralBody body) {
    Optional<Planet> sphereOfInfluence = findSphereOfInfluence(body);
    if (sphereOfInfluence.isEmpty()) {
      return;
    }
    OrbitalState state =
        new OrbitalStateBuilder(body.getMotionState(), sphereOfInfluence.get()).getVectors();
    body.setMotionState(state);
  }

  private Optional<Planet> findSphereOfInfluence(AstralBody satellite) {
    if (satellite instanceof Craft craft) {
      return Optional.of(maximumAccelerationValue(craft));
    }
    return satellite.getSphereOfInfluence().map(orrery::getPlanetByName);
  }

  private Planet maximumAccelerationValue(Craft craft) {
    MotionState craftState = craft.getMotionState();
    List<Planet> planets = orrery.getAllPlanets();

    Planet sphereOfInfluence =
        planets.stream()
            .map(planet -> findAccelerationTowards(craftState, planet))
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow();

    craft.setSphereOfInfluence(sphereOfInfluence.getBodyType());

    return sphereOfInfluence;
  }

  private Map.Entry<Planet, Double> findAccelerationTowards(
      MotionState satelliteState, Planet focusPlanet) {
    MotionState focusBodyState = focusPlanet.getMotionState();
    double focusBodyMu = focusPlanet.getMu();
    double distance = focusBodyState.getPosition().subtract(satelliteState.getPosition()).getNorm();
    double acceleration = focusBodyMu / Math.pow(distance, 2);
    return Map.entry(focusPlanet, acceleration);
  }

  public void centreBody(AstralBody body) {
    Vector3D positionShiftVector = body.getMotionState().getPosition().negate();
    Vector3D velocityShiftVector = body.getMotionState().getVelocity().negate();
    orrery.getAstralBodies().stream()
        .map(AstralBody::getMotionState)
        .forEach(
            motionState -> {
              motionState.setPosition(motionState.getPosition().add(positionShiftVector));
              motionState.setVelocity(motionState.getVelocity().add(velocityShiftVector));
            });
  }
}
