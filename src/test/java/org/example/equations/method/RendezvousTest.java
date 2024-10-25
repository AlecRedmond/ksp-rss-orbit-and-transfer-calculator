package org.example.equations.method;

import org.example.equations.application.Intercept;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class RendezvousTest {

  @Test
  void testRendezvous() {
    OrbitBuilder orbitBuilder = new OrbitBuilder(200000,200000);
    Orbit orbit = orbitBuilder.getOrbit();
    double orbitTime = orbit.getDataFor(Kepler.KeplerEnums.ORBITAL_PERIOD);
    double orbitalProcessionDayRads = Math.toRadians(0);
    System.out.println(orbitTime);
    Rendezvous rendezvous = new Rendezvous(orbitTime, orbitalProcessionDayRads);
    ArrayList<String> interceptTimeStrings = new ArrayList<>();
    ArrayList<String> leadTimeStrings = new ArrayList<>();
    for (Intercept intercept : rendezvous.getIntercepts()) {
      interceptTimeStrings.add(intercept.getInterceptTimeString());
      leadTimeStrings.add(intercept.getSatelliteLeadTimeString());
    }
    System.out.println(interceptTimeStrings);
    System.out.println(leadTimeStrings);
  }
}
