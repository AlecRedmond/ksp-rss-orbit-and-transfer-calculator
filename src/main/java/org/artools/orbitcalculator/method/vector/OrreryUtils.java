package org.artools.orbitcalculator.method.vector;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.BodyType;
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

  public OrreryUtils() {
    this.orrery = new Orrery();
  }

  public Orrery convertToOrbitalStates() {
    orrery.getAstralBodies().forEach(this::convertToOrbitalState);
    return orrery;
  }

  private void convertToOrbitalState(AstralBody body) {
    if(body.getBodyType().equals(BodyType.SUN)){
      return;
    }
    Planet orbitalFocus = findOrbitalFocus(body);
    OrbitalState state = new OrbitalStateBuilder(body.getMotionState(), orbitalFocus).getVectors();
    body.setMotionState(state);
  }

  private Planet findOrbitalFocus(AstralBody satellite) {
    if (satellite instanceof Planet) {
      BodyType parentBodyType = ((Planet) satellite).getParentBody();
      return orrery.getAstralBodies().stream()
          .filter(body -> body.getBodyType().equals(parentBodyType))
          .findFirst()
          .map(Planet.class::cast)
          .orElseThrow();
    }
    return maximumAccelerationValue(satellite);
  }

  private Planet maximumAccelerationValue(AstralBody satellite) {
    MotionState satelliteState = satellite.getMotionState();
    List<Planet> planets = orrery.getAllPlanets();

    return planets.stream()
        .filter(body -> !body.equals(satellite))
        .map(planet -> findAccelerationTowards(satelliteState, planet))
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElseThrow();
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
    Vector3D shiftVector = body.getMotionState().getPosition().negate();
    adjustAllBy(shiftVector);
  }

  private void adjustAllBy(Vector3D shiftVector) {
    orrery.getAstralBodies().stream()
        .map(AstralBody::getMotionState)
        .forEach(
            motionState -> motionState.setPosition(motionState.getPosition().add(shiftVector)));
  }
}
