package org.example.equations.application;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.equations.application.keplerianelements.Periapsis;
import org.junit.jupiter.api.Test;

class OrbitTest {
  Orbit orbit = new Orbit();
  Periapsis periapsis = new Periapsis();
  double testDouble = 300000;
  String testString = "300 km";

  @Test
  void setFromString() {
    orbit.setFromString(testString,periapsis);
    assertEquals(testDouble, orbit.getDataFor(PERIAPSIS));
  }
}
