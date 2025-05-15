package org.example.equations.method.utility;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class VectorUtilsTest {
  VectorUtils test = new VectorUtils(Body.EARTH);

  @Test
  void velocityVector() {
    Orbit orbit = new OrbitBuilder(250000, 35786000).getOrbit();
    assertEquals(
        orbit.getDataFor(Kepler.KeplerEnums.VELOCITY_PERIAPSIS),
        getVelocityVector(orbit, 0).getNorm(),
        1e-6);
    IntStream.range(0,361).filter(i -> i % 45 == 0).forEach(i -> System.out.println(getVelocityVector(orbit,i)));
  }

  Vector3D getVelocityVector(Orbit orbit, double trueAnomalyDegrees) {
    double trueAnomaly = Math.toRadians(trueAnomalyDegrees);
    return test.velocityVector(orbit, trueAnomaly);
  }
}
