package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeplerBuilderTest {
  Orrery orrery;
  KeplerBuilder test;

  @BeforeEach
  void init() {
    orrery = new OrreryBuilder().getOrrery();
    test = new KeplerBuilder(Instant.parse("1951-01-01T00:00:00.00Z"), BodyType.EARTH, orrery);
  }

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
