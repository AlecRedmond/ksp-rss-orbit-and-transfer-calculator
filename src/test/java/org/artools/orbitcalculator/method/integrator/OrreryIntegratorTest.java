package org.artools.orbitcalculator.method.integrator;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Body;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.application.writeableorbit.keplerianelements.Kepler;
import org.artools.orbitcalculator.method.vector.OrbitalStateBuilder;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class OrreryIntegratorTest {
  OrreryIntegrator test;

  @BeforeEach
  void initialise() {
    Orrery orrery = new OrreryBuilder().setTo1951Jan1().getOrrery();
    test = new OrreryIntegrator(orrery);
  }

  @Test
  void stepForward() {
    Map<Integer, Vector3D> sunPositions = new HashMap<>();
    IntStream.range(0, 51)
        .forEach(
            i -> {
              sunPositions.put(i, test.getOrrery().getMotionVectors(Body.SUN).getPosition());
              test.stepForward(Duration.of(365, ChronoUnit.DAYS));
            });
    sunPositions.forEach((k, v) -> System.out.println((1951 + k) + " : " + (v.getNorm())/(2 * Body.SUN.getRadius())));
    var max =
        sunPositions.entrySet().stream()
            .max(Comparator.comparing(entry -> entry.getValue().getNorm()))
            .get()
            .getKey();
    System.out.println("Max is " + (max + 1951));
  }

  @Test
  void stepForward2() {
      Instant start = Instant.now();
    Map<Integer, Orbit> earthOrbits = new HashMap<>();
    IntStream.range(0, 101)
        .forEach(
            i -> {
              var earthMotionState = test.getOrrery().getMotionVectors(Body.EARTH);
              var earthOrbit =
                  new OrbitalStateBuilder().buildVectors(earthMotionState).getAsOrbit();
              earthOrbits.put(i, earthOrbit);
              test.stepForward(Duration.of(365, ChronoUnit.DAYS));
            });
    Instant end = Instant.now();
    earthOrbits.forEach(
        (k, v) ->
            System.out.println((k + 1951) + " : " + v.getAsString(Kepler.KeplerEnums.ORBITAL_PERIOD)));

    var summedOrbitTime = earthOrbits.values().stream().map(orbit -> orbit.getDataFor(Kepler.KeplerEnums.ORBITAL_PERIOD)).reduce(0.0, Double::sum);
    var averageOrbitTime = summedOrbitTime / earthOrbits.size();
    Orbit orbit = new Orbit();
    orbit.setDataFor(Kepler.KeplerEnums.ORBITAL_PERIOD,averageOrbitTime);
    System.out.println("Average : " + orbit.getAsString(Kepler.KeplerEnums.ORBITAL_PERIOD));
    long millis = Duration.between(start,end).toMillis();
    System.out.println("Completed in " + millis + "ms");
  }

  @Test
  void stepToDate() {}
}
