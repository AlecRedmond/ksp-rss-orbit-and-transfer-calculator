package org.example.equations.method.vector;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CraftVectorControllerTest {
  CraftVectorController test = new CraftVectorController();

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

  Vector3D getRadiusVector(Orbit orbit, double trueAnomalyDegrees) {
    double trueAnomaly = Math.toRadians(trueAnomalyDegrees);
    return test.radiusVector(orbit, trueAnomaly);
  }

  @Test
  void radiusVector() {
    Orbit orbit = new OrbitBuilder(250000, 35786000).getOrbit();
    assertEquals(
            orbit.getDataFor(Kepler.KeplerEnums.PERIAPSIS) + orbit.getBody().getRadius(),
            getRadiusVector(orbit, 0).getNorm(),
            1e-6);
    IntStream.range(0,361).filter(i -> i % 45 == 0).forEach(i -> System.out.println(getRadiusVector(orbit,i)));
    }
}
