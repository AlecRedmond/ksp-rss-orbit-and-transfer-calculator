package org.example.equations.method.integrator;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.example.equations.application.Body;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.application.vector.MotionVectors;
import org.example.equations.application.vector.Orrery;
import org.example.equations.method.vector.OrbitalVectorBuilder;
import org.example.equations.method.vector.OrreryBuilder;
import org.junit.jupiter.api.Test;

class OrreryIntegratorTest {
  Orrery orrery = new OrreryBuilder().setTo1951Jan1().getOrrery();
  OrreryIntegrator test = new OrreryIntegrator(orrery);

  @Test
  void stepForward() {
    Map<Body, Orbit> orbitMap = new EnumMap<>(Body.class);
    putOrbits(orbitMap);
    var oneDay = 3600 * 24;
    test.stepForward(oneDay * 365);
    Map<Body, Orbit> orbitMap2 = new EnumMap<>(Body.class);
    putOrbits(orbitMap2);
    compareMaps(orbitMap, orbitMap2);
  }

  private void putOrbits(Map<Body, Orbit> orbitMap) {
    List<Body> bodies =
        orrery.getMap().keySet().stream().filter(body -> !body.equals(Body.SUN)).toList();
    bodies.forEach(
        body -> {
          MotionVectors mv = orrery.getMotionVectors(body);
          Orbit orbit = new OrbitalVectorBuilder().buildVectors(mv).getAsOrbit();
          orbitMap.put(body, orbit);
        });
  }

  private void compareMaps(Map<Body, Orbit> orbitMap, Map<Body, Orbit> orbitMap2) {
    List<Body> bodies = orbitMap.keySet().stream().filter(body -> !body.equals(Body.MOON)).toList();
    bodies.forEach(
        body -> {
          System.out.println("CHECKING " + body + "...");
          assertInMoE(body, SEMI_MAJOR_AXIS, orbitMap, orbitMap2);
          System.out.println("SMA OK!\n---");
        });
  }

  private void assertInMoE(
      Body body,
      Kepler.KeplerEnums keplerEnums,
      Map<Body, Orbit> orbitMap,
      Map<Body, Orbit> orbitMap2) {
    var percentile = 0.02;
    var data = orbitMap.get(body).getDataFor(keplerEnums);
    var data2 = orbitMap2.get(body).getDataFor(keplerEnums);
    var delta = Math.abs(data * percentile);
    assertEquals(data, data2, delta);
  }
}
