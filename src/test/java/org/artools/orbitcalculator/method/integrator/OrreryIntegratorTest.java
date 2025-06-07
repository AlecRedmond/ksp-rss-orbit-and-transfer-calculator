package org.artools.orbitcalculator.method.integrator;

import static org.artools.orbitcalculator.application.bodies.Body.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.artools.orbitcalculator.application.bodies.Body;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.artools.orbitcalculator.method.vector.OrreryUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrreryIntegratorTest {
  static final List<Integer> yearsToTest = List.of(1951, 1971, 1991, 2011, 2021, 2031, 2041, 2051);
  static final int TESTS_PER_YEAR = 8;
  static final double AVERAGE_DELTA_RATIO = 10E-3;
  static OrreryIntegrator test;
  static List<Orrery> orreries;

  public static Stream<Arguments> provideSemiMajorAxisValidation() {
    return Stream.of(Arguments.of(EARTH, 149.598E9),
            Arguments.of(JUPITER,778.479E9),
            Arguments.of(SATURN,1432.041E9),
            Arguments.of(URANUS,2867.043E9),
            Arguments.of(NEPTUNE,4514.953E9));
  }

  @BeforeAll
  static void initialise() {
    Orrery orrery = new OrreryBuilder().setTo1951Jan1().getOrrery();
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
    Instant end = instant.plus(365, ChronoUnit.DAYS);
    long timeBetween = instant.until(end, ChronoUnit.SECONDS);
    long stepSeconds = timeBetween / TESTS_PER_YEAR;
    return IntStream.range(0, TESTS_PER_YEAR)
        .mapToObj(integer -> instant.plus(integer * stepSeconds, ChronoUnit.SECONDS));
  }

  @ParameterizedTest
  @MethodSource("provideSemiMajorAxisValidation")
  void validateOrbitsAverageSMA(Body body, double expectedSMA) {
    List<Double> smaList = orreries.stream().map(orrery -> orrery.getOrbitalVectors(body).getSemiMajorAxis()).toList();
    double averageSMA = smaList.stream().reduce(0.0,Double::sum) / smaList.size();
    assertEquals(expectedSMA,averageSMA,expectedSMA * AVERAGE_DELTA_RATIO);
  }
}
