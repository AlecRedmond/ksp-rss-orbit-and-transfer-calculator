package org.example.equations.application.keplerianelements;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApoapsisTest {
  static double testData = 35768e3;
  static String testString = "35.768 Mm";
  static Apoapsis apoapsis;

  @BeforeAll
  static void generateApoapsis(){
    apoapsis = new Apoapsis();
  }

  @Test
  void set() {
    apoapsis.set(testData);
    assertEquals(testData,apoapsis.get());
  }

  @Test
  void getAsString() {
    apoapsis.set(testData);
    String test = apoapsis.getAsString();
    assertEquals(testString,test);
  }

  @Test
  void setFromString() {
    apoapsis.setFromString(testString);
    assertEquals(testData,apoapsis.get());
  }
}
