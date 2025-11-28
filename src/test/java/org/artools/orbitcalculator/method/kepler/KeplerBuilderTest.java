package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.artools.orbitcalculator.application.bodies.planets.Earth;
import org.junit.jupiter.api.Test;

class KeplerBuilderTest {
  KeplerBuilder test = new KeplerBuilder(Instant.parse("1951-01-01T00:00:00.00Z"), new Earth());

  @Test
  void setData() {
    test.setData(APOAPSIS, 35_786_000);
    test.setData(PERIAPSIS, 600_000);
    assertFalse(test.isBuilt());
    test.setData(INCLINATION, 0);
    test.setData(LONGITUDE_ASCENDING_NODE, 0);
    test.setData(ARGUMENT_OF_PERIAPSIS, 0);
    assertFalse(test.isBuilt());
    test.setData(TRUE_ANOMALY, Math.toRadians(-45));
    assertTrue(test.isBuilt());
    test.getOrbit().getElementsMap().entrySet().stream()
        .map(entry -> entry.getKey() + " : " + entry.getValue())
        .forEach(System.out::println);
  }
}
