package org.artools.orbitcalculator.application.vector;

import java.time.Instant;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.BodyType;
import org.artools.orbitcalculator.application.bodies.planets.Planet;

@Data
@NoArgsConstructor
public class Orrery {
  private List<AstralBody> astralBodies = new ArrayList<>();

  public Orrery(List<Planet> planets) {
    astralBodies.addAll(planets);
  }

  public List<Planet> getAllPlanets() {
    return astralBodies.stream().filter(Planet.class::isInstance).map(Planet.class::cast).toList();
  }

  public Optional<Planet> getPlanetByName(BodyType name) {
    if (name.equals(BodyType.CRAFT)) {
      return Optional.empty();
    }
    return astralBodies.stream()
        .filter(body -> body.getBodyType().equals(name))
        .filter(Planet.class::isInstance)
        .map(Planet.class::cast)
        .findFirst();
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
