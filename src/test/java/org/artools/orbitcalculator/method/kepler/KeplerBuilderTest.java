package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;
import static org.artools.orbitcalculator.method.kepler.KeplerUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.artools.orbitcalculator.application.kepler.KeplerHolds;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeplerBuilderTest {
  static final Instant EPOCH = Instant.parse("1951-01-01T00:00:00.00Z");
  static final BodyType CENTRAL_BODY_TYPE = BodyType.EARTH;
  Planet centralBody;
  Orrery orrery;
  KeplerBuilder test;
  KeplerOrbit orbit;
  KeplerHolds holds;

  @BeforeEach
  void init() {
    orrery = new OrreryBuilder().getOrrery();
    centralBody = orrery.getPlanetByType(CENTRAL_BODY_TYPE);
    orbit = new KeplerOrbit(Timestamp.from(EPOCH), CENTRAL_BODY_TYPE);
    holds = new KeplerHolds();
  }

  @Test
  void setData() {
    orbit.setData(APOAPSIS, 35_786_000);
    orbit.setData(PERIAPSIS, 600_000);
    copyOrbitKeysToHolds(ELLIPTICAL_ELEMENTS);
    test = new KeplerBuilder(orbit, centralBody, holds);
    orbit = test.getOrbit();
    assertTrue(holds.isEllipticSolvable());
    assertFalse(holds.isRotationalSolvable());
    assertFalse(holds.isPositionSolvable());
    assertFalse(orbit.isAllElementsBuilt());

    checkOrbitContains(List.of(ECCENTRICITY, SEMI_MAJOR_AXIS, ORBITAL_PERIOD));

    orbit.setData(INCLINATION, 0);
    orbit.setData(LONGITUDE_ASCENDING_NODE, 0);
    orbit.setData(ARGUMENT_OF_PERIAPSIS, 0);
    copyOrbitKeysToHolds(ROTATIONAL_ELEMENTS);
    test = new KeplerBuilder(orbit, centralBody, holds);
    orbit = test.getOrbit();
    assertTrue(holds.isEllipticSolvable());
    assertTrue(holds.isRotationalSolvable());
    assertFalse(holds.isPositionSolvable());
    assertFalse(orbit.isAllElementsBuilt());

    orbit.setData(MEAN_ANOMALY, Math.toRadians(135));
    // orbit.setData(TIME_TO_PERIAPSIS,38331.88654612976);
    copyOrbitKeysToHolds(POSITION_ELEMENTS);
    test = new KeplerBuilder(orbit, centralBody, holds);
    orbit = test.getOrbit();
    assertTrue(holds.isEllipticSolvable());
    assertTrue(holds.isRotationalSolvable());
    assertTrue(holds.isPositionSolvable());
    assertTrue(orbit.isAllElementsBuilt());

    checkOrbitContains(List.of(MEAN_ANOMALY, ECCENTRIC_ANOMALY, TRUE_ANOMALY, TIME_TO_PERIAPSIS));

    test.getOrbit().getElementsMap().entrySet().stream()
        .map(entry -> entry.getKey() + " : " + entry.getValue())
        .forEach(System.out::println);
    Timestamp timestamp = test.getOrbit().getTimestamp();
    System.out.println(timestamp);
  }

  private void copyOrbitKeysToHolds(Set<KeplerElement> elementType) {
    orbit.getElementsMap().keySet().stream()
        .filter(elementType::contains)
        .forEach(e -> KeplerHoldsLogic.addHold(holds, e));
  }

  private void checkOrbitContains(Collection<KeplerElement> elements) {
    elements.forEach(element -> assertTrue(orbit.containsKey(element)));
  }
}
