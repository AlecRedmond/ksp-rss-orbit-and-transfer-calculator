package org.example.equations.method.vector;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

class FrameTransformTest {
  FrameTransform test = new FrameTransform();
  Vector3D xAxisVector = Vector3D.PLUS_I;
  Vector3D yAxisVector = Vector3D.PLUS_J;
  Vector3D zAxisVector = Vector3D.PLUS_K;

  Orbit polarOrbit() {
    return new OrbitBuilder(250e3, 250e3, 90, 90, 90).getOrbit();
  }

  void assertVectorsEqual(Vector3D vector1, Vector3D vector2) {
    assertEquals(vector1.getX(), vector2.getX(), 1e-6);
    assertEquals(vector1.getY(), vector2.getY(), 1e-6);
    assertEquals(vector1.getZ(), vector2.getZ(), 1e-6);
  }

  Orbit equatorialOrbit(){
    return new OrbitBuilder(250e3, 250e3 ).getOrbit();
  }

  Orbit molniyaOrbit(){
    return new OrbitBuilder(600e3, 35786e3, 90, 63.4, 270).getOrbit();
  }


  @Test
  void intersect() {
    Orbit orbitA = polarOrbit();
    Orbit orbitB = new Orbit();
    var lineOptional = test.intersect(orbitA, orbitB);
    assertTrue(lineOptional.isPresent());
    var line = lineOptional.get();
    System.out.println(line.getDirection());
    System.out.println(line.getDirection().negate());
  }

  void setArguments(
      Orbit orbit,
      double rightAscensionDegrees,
      double inclinationDegrees,
      double argumentPEDegrees) {
    orbit.setDataFor(Kepler.KeplerEnums.RIGHT_ASCENSION, Math.toRadians(rightAscensionDegrees));
    orbit.setDataFor(Kepler.KeplerEnums.INCLINATION, Math.toRadians(inclinationDegrees));
    orbit.setDataFor(Kepler.KeplerEnums.ARGUMENT_PE, Math.toRadians(argumentPEDegrees));
  }

  
}
