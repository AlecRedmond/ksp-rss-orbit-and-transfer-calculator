package org.example.equations.application.keplerianelements;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VelocityTest {

  static double testData = 90;
  static String testString = "90.00 m/s";
  static Velocity velocity;

  @BeforeAll
  static void generateVelocity() {
    velocity = new Velocity();
  }

  @Test
  void set() {
    velocity.set(testData);
    assertEquals(testData, velocity.get());
  }

  @Test
  void getAsString() {
    velocity.set(testData);
    String test = velocity.getAsString();
    assertEquals(testString, test);
  }

  @Test
  void setFromString() {
    velocity.setFromString(testString);
    assertEquals(testData, velocity.get());
  }
}
