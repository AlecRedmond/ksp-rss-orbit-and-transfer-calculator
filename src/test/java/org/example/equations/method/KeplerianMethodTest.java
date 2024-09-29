package org.example.equations.method;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.example.equations.application.Keplerian;
import org.example.equations.application.keplerianelements.Apoapsis;
import org.example.equations.application.keplerianelements.OrbitalPeriod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class KeplerianMethodTest {

  private static Keplerian keplerian = new Keplerian();
  private static HashMap<Class, String> dataToParse = new HashMap<>(Map.of(
          OrbitalPeriod.class,"1:29:04.5",
          Apoapsis.class,"250 km"
  ));
  private static KeplerianMethod keplerianMethod;

  @BeforeAll
  static void setKeplerianMethod(){
    keplerianMethod = new KeplerianMethod();
    keplerianMethod.setKeplerian(keplerian);
    keplerianMethod.setDataToParse(dataToParse);
  }

  @Test
  void calculateMissing() {
    keplerianMethod.calculateMissing();
  }

  @Test
  void testCalculateMissing() {}
}
