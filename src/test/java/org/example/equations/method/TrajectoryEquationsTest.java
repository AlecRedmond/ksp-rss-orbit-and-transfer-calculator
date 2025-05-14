package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import org.example.equations.application.Orbit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TrajectoryEquationsTest {

  static double orbitalPeriod = 60*60*1.5;
  static Orbit orbit;

  @BeforeAll
  public static void startUp(){
    orbit = new Orbit();
    orbit.setDataFor(ORBITAL_PERIOD, orbitalPeriod);
  }

  @Test
  void convertOrbitalPeriod() {
    TrajectoryEquations.convertOrbitalPeriodToSMA(orbit);
    System.out.println(orbit.getDataFor(SEMI_MAJOR_AXIS));
  }
}
