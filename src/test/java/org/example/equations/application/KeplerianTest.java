package org.example.equations.application;

import static org.example.equations.application.keplerianelements.Kepler.KeplarianElement.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.equations.application.keplerianelements.Periapsis;
import org.junit.jupiter.api.Test;

class KeplerianTest {
  Keplerian keplerian = new Keplerian();
  Periapsis periapsis = new Periapsis();
  double testDouble = 300000;
  String testString = "300 km";

  @Test
  void setFromString() {
    keplerian.setFromString(testString,periapsis);
    assertEquals(testDouble,keplerian.getDataFor(PERIAPSIS));
  }
}
