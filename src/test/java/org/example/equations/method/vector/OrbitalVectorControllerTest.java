package org.example.equations.method.vector;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class OrbitalVectorControllerTest {
  OrbitalVectorController test = new OrbitalVectorController();
  Orbit molniyaOrbit = new OrbitBuilder(600e3,35786e3,90,64.3,270).getOrbit();

  @Test
  void buildVectors() {
    var motionVectors = new CraftVectorController().buildVectors(molniyaOrbit,0).getSOIVectors();
    var vectors = test.buildVectors(motionVectors.get()).getVectors();
    System.out.println(vectors);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.RIGHT_ASCENSION),vectors.getRightAscension(),1e-3);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY),vectors.getEccentricity().getNorm(),1e-3);
    assertEquals(molniyaOrbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS),vectors.getSemiMajorAxis(),1e-3);
  }
}
