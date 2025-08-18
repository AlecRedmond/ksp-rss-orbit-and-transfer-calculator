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
  private Map<BodyType, Integer> planetIndex;

  public Orrery() {
    astralBodies = new ArrayList<>();
    planetIndex = new EnumMap<>(BodyType.class);
  }

  public Orrery(List<Planet> planets) {
    astralBodies = new ArrayList<>();
    planetIndex = new EnumMap<>(BodyType.class);
    for (int i = 0; i < planets.size(); i++) {
      Planet planet = planets.get(i);
      astralBodies.add(planet);
      planetIndex.put(planet.getBodyType(), i);
    }
  }

  public List<Planet> getAllPlanets() {
    return planetIndex.values().stream().map(astralBodies::get).map(Planet.class::cast).toList();
  }

  public Planet getPlanetByName(BodyType name) {
    int index = planetIndex.get(name);
    return (Planet) astralBodies.get(index);
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
