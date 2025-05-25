package org.example.equations.method.vector;

import org.example.equations.application.Body;
import org.example.equations.application.BodyOrbits1951;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.example.equations.application.vector.MotionVectors.Frame.BODY_INERTIAL_FRAME;
import static org.junit.jupiter.api.Assertions.*;

class OrbitalVectorBuilderTest {
  OrbitalVectorBuilder test = new OrbitalVectorBuilder();
  Orbit molniyaOrbit = new OrbitBuilder(600e3,35786e3,0,90,270).getOrbit();
  Orbit polarOrbit = new OrbitBuilder(250e3,500e3,0,90,0).getOrbit();
  Instant epoch = Instant.parse("1951-01-01T00:00:00.00Z");

  @Test
  void buildVectors() {
    var motionVectors = new MotionVectorBuilder().buildVectors(molniyaOrbit,0,epoch, BODY_INERTIAL_FRAME).getSOIVectors();
    var vectors = test.buildVectors(motionVectors.get()).getVectors();
    System.out.println(vectors);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.RIGHT_ASCENSION),vectors.getRightAscension(),1e-3);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY),vectors.getEccentricity().getNorm(),1e-3);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS),vectors.getSemiMajorAxis(),1e-3);
  }

  @Test
  void buildVectors2() {
    var vectors = BodyOrbits1951.getOrbitalVectors(Body.EARTH);
    test.setVectors(vectors).getAsOrbit().printAll();
  }

  @Test
  void getAsOrbit() {
    var motionVectors = new MotionVectorBuilder().buildVectors(polarOrbit,0,epoch, BODY_INERTIAL_FRAME).getSOIVectors();
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
