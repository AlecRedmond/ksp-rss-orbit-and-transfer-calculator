package org.example.equations.method;

import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodalPrecessionMethodTest {
  double inclinationDegs = 90;
  double apoapsis = 900000;
  double periapsis = 250000;
  double desiredPrecessionDegs = 0.98;

  @Test
  void NodalProcessionMethod() {

    OrbitBuilder orbitBuilder = new OrbitBuilder(periapsis, apoapsis);
    Orbit orbit = orbitBuilder.getOrbit();
    NodalPrecessionMethod nodalPrecessionMethod =
        new NodalPrecessionMethod(orbit, Math.toRadians(desiredPrecessionDegs));
    orbit = nodalPrecessionMethod.getOrbit();
    String inclination = orbit.getAsString(Kepler.KeplerEnums.INCLINATION);
    String nodalPrecession = orbit.getAsString(Kepler.KeplerEnums.NODAL_PRECESSION);
    System.out.println(inclination + "\n" + nodalPrecession);
  }
}
