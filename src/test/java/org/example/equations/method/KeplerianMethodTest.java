package org.example.equations.method;

import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;
import org.example.equations.application.keplerianelements.Apoapsis;
import org.example.equations.application.keplerianelements.Eccentricity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeplerianMethodTest {

  private static Keplerian keplerian;
  private static KeplerianMethod keplerianMethod;

  @BeforeAll
  static void setKeplerianMethod(){
    keplerian = new Keplerian();
    keplerian.setEccentricity(0.0);
    keplerian.setApoapsis(250e3);
    keplerian.setHold(true, Eccentricity.class);
    keplerian.setHold(true, Apoapsis.class);
    keplerianMethod = new KeplerianMethod();
    keplerianMethod.setKeplerian(keplerian);
  }

  @Test
  void calculateMissing() {
    keplerianMethod.calculateMissing();
    double periapsis = keplerianMethod.getKeplerian().getPeriapsis();
    assertEquals(250e3,periapsis);
  }
}
