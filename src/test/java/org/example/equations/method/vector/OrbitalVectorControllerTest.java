package org.example.equations.method.vector;

import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrbitalVectorControllerTest {
  OrbitalVectorController test = new OrbitalVectorController();
  Orbit molniyaOrbit = new OrbitBuilder(600e3,35786e3,0,90,270).getOrbit();
  Orbit polarOrbit = new OrbitBuilder(250e3,500e3,0,90,0).getOrbit();

  @Test
  void buildVectors() {
    var motionVectors = new CraftVectorController().buildVectors(molniyaOrbit,0).getSOIVectors();
    var vectors = test.buildVectors(motionVectors.get()).getVectors();
    System.out.println(vectors);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.RIGHT_ASCENSION),vectors.getRightAscension(),1e-3);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY),vectors.getEccentricity().getNorm(),1e-3);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS),vectors.getSemiMajorAxis(),1e-3);
  }

  @Test
  void getAsOrbit() {
    var motionVectors = new CraftVectorController().buildVectors(polarOrbit,0).getSOIVectors();
    Orbit testOrbit = test.buildVectors(motionVectors.get()).getAsOrbit();
    testOrbit.getKeplarianElements().entrySet().forEach(entry -> {
      var key = entry.getKey();
      var actualVal = entry.getValue().getData();
      var expectedVal = polarOrbit.getDataFor(key);
      System.out.println("Checking " + key.toString());
      assertEquals(expectedVal,actualVal,1e-6);
      System.out.println(key.toString() + " valid!");
    });
  }
}
