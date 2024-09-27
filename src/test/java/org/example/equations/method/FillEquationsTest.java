package org.example.equations.method;

import org.example.equations.application.Keplerian;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FillEquationsTest {

  static double orbitalPeriod = 60*60*1.5;
  static Keplerian keplerian;

  @BeforeAll
  public static void startUp(){
    keplerian = new Keplerian();
    keplerian.getOrbitalPeriod().set(orbitalPeriod);
  }

  @Test
  void convertOrbitalPeriod() {
    FillEquations.convertOrbitalPeriod(keplerian);
    System.out.println(keplerian.getSemiMajorAxis().get());
  }
}
