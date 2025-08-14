package org.artools.orbitcalculator.method.vector;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;

import org.artools.orbitcalculator.application.bodies.BodyType;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.junit.jupiter.api.Test;

class OrreryBuilderTest {
  OrreryBuilder test = new OrreryBuilder();

  @Test
  void setToStartingEpoch() {
    //ASSERT NONE ARE NULL
    Orrery orrery = test.getOrrery();
    Arrays.stream(BodyType.values())
        .filter(ab -> !ab.equals(BodyType.CRAFT))
        .forEach(
            bodyType -> {
              Optional<Planet> optionalPlanet = orrery.getPlanetByName(bodyType);
              assertFalse(optionalPlanet.isEmpty());
            });
  }
}
