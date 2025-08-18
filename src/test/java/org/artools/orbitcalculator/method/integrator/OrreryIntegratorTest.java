package org.artools.orbitcalculator.method.integrator;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.artools.orbitcalculator.method.vector.OrreryUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrreryIntegratorTest {
  static final List<Integer> yearsToTest = IntStream.range(1951, 2051).boxed().toList();
  static final int TESTS_PER_YEAR = 4;
  static final double AVERAGE_DELTA_RATIO = 5E-3;
  static OrreryIntegrator test;
  static List<Orrery> orreries;

  public static Stream<Arguments> provideValidSMAs(){
    return Stream.of(
            Arguments.of(BodyType.EARTH, 149.598E9),
            Arguments.of(BodyType.JUPITER, 778.479E9),
            Arguments.of(BodyType.SATURN, 1.426955795169579E12),
            Arguments.of(BodyType.URANUS, 2.871309893991385E12),
            Arguments.of(BodyType.NEPTUNE, 4.498638029781399E12));
  }

  @BeforeAll
  static void initialise() {
    Orrery orrery = new OrreryBuilder().getOrrery();
    test = new OrreryIntegrator(orrery);
    orreries =
        yearsToTest.stream()
            .map(integer -> integer + "-01-01T00:00:00.00Z")
            .map(Instant::parse)
            .flatMap(OrreryIntegratorTest::getInstants)
            .map(instant -> test.stepToDate(instant).getOrrery())
            .map(thisOrrery -> new OrreryUtils(thisOrrery).convertToOrbitalStates())
            .toList();
  }

  private static Stream<Instant> getInstants(Instant instant) {
    Instant end = instant.plus(365, ChronoUnit.DAYS).plus(6, ChronoUnit.HOURS);
    long timeBetween = instant.until(end, ChronoUnit.SECONDS);
    long stepSeconds = timeBetween / TESTS_PER_YEAR;
    return IntStream.range(0, TESTS_PER_YEAR)
        .mapToObj(integer -> instant.plus(integer * stepSeconds, ChronoUnit.SECONDS));
  }

  @ParameterizedTest
  @MethodSource("provideValidSMAs")
  void validateOrbitsAverageSMA(BodyType bodyType, double expectedSMA) {
    List<Double> smaList =
        orreries.stream()
            .map(orrery -> orrery.getPlanetByName(bodyType))
            .map(Planet::getMotionState)
            .filter(OrbitalState.class::isInstance)
            .map(OrbitalState.class::cast)
            .map(OrbitalState::getSemiMajorAxis)
            .toList();
    double averageSMA = smaList.stream().reduce(0.0, Double::sum) / smaList.size();
    assertEquals(expectedSMA, averageSMA, expectedSMA * AVERAGE_DELTA_RATIO);
  }
}
