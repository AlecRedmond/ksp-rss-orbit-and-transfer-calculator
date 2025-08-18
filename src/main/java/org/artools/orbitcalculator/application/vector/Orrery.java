package org.artools.orbitcalculator.application.vector;

import java.time.Instant;
import java.util.*;
import lombok.Data;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.bodies.planets.Planet;

@Data
public class Orrery {
  private List<AstralBody> astralBodies;

  public Orrery(List<Planet> planets) {
    astralBodies = new ArrayList<>(planets);
  }

  public List<Planet> getAllPlanets() {
    return astralBodies.stream().filter(Planet.class::isInstance).map(Planet.class::cast).toList();
  }

  public Planet getPlanetByType(BodyType type) {
    return astralBodies.stream()
        .filter(Planet.class::isInstance)
        .map(Planet.class::cast)
        .filter(p -> p.getBodyType().equals(type))
        .findAny()
        .orElseThrow();
  }

  public Instant getEpoch() {
    return epochsAreEqual() ? firstInstant() : null;
  }

  private boolean epochsAreEqual() {
    Instant firstInstant = firstInstant();
    return astralBodies.stream()
        .map(AstralBody::getMotionState)
        .allMatch(motionState -> motionState.getEpoch().equals(firstInstant));
  }

  private Instant firstInstant() {
    return astralBodies.stream()
        .findFirst()
        .map(AstralBody::getMotionState)
        .map(MotionState::getEpoch)
        .orElse(null);
  }
}
