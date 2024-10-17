package org.example.equations.application;

import org.example.equations.application.keplerianelements.Periapsis;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeplerianTest {
  Keplerian keplerian = new Keplerian();
  Periapsis periapsis = new Periapsis();
  double testDouble = 300000;
  String testString = "300 km";

  @Test
  void setFromString() {
    keplerian.setFromString(testString,periapsis);
    assertEquals(testDouble,keplerian.getPeriapsis().get());
  }
}
