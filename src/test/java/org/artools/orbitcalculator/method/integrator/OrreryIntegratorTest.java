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
    Map<Integer, Orbit> jupiterOrbits = new HashMap<>();
    IntStream.range(0, 51)
        .forEach(
            i -> {
              var jupiterMotionState = test.getOrrery().getMotionVectors(Body.JUPITER);
              var jupiterOrbit =
                  new OrbitalStateBuilder().buildVectors(jupiterMotionState).getAsOrbit();
              jupiterOrbits.put(i, jupiterOrbit);
              test.stepForward(Duration.of(365, ChronoUnit.DAYS));
            });
    jupiterOrbits.forEach(
        (k, v) ->
            System.out.println(k + " year : " + v.getAsString(Kepler.KeplerEnums.SEMI_MAJOR_AXIS)));
  }

  @Test
  void stepToDate() {}
}
