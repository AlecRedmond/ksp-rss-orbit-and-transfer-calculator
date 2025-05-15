package org.example.equations.method.referenceframes;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

class AngleTransformTest {
  AngleTransform test = new AngleTransform();
  Vector3D xAxisVector = Vector3D.PLUS_I;
  Vector3D yAxisVector = Vector3D.PLUS_J;
  Vector3D zAxisVector = Vector3D.PLUS_K;

  @Test
  void toPerifocalFrame() {
    Orbit orbit = polarOrbit();
    Vector3D newVector = test.toPerifocalFrame(xAxisVector, orbit);
    // After a 90°/90°/90° Z-X-Z euler rotation, the new x Axis should be in the Z axis frame
    assertVectorsEqual(newVector, zAxisVector);
  }

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
  void toInertialFrame() {
    Orbit orbit = polarOrbit();
    Vector3D newVector = test.toPerifocalFrame(zAxisVector, orbit);
    // simple reversal of the earlier test
    assertVectorsEqual(newVector, xAxisVector);
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

  @Test
  void perifocalRadiusVector() {
    double radius = 1;
    double trueAnomaly = Math.toRadians(90);
    Vector3D actual = test.perifocalRadiusVector(radius, trueAnomaly);
    //90° from the X axis should be the Y axis
    assertVectorsEqual(yAxisVector, actual);
  }

  @Test
  void intersectTrueAnomaly() {
    Orbit orbitA = equatorialOrbit();
    Orbit orbitB = equatorialOrbit();
    setArguments(orbitA, 0, 5.25, 0);
    setArguments(orbitB, 10, 63.4, 0);
    var anomaliesOptional = test.intersectTrueAnomaly(orbitA, orbitB);
    if (anomaliesOptional.isEmpty()) {
      System.out.println("Coplanar");
      return;
    }
    var anomalies = anomaliesOptional.get();
    for (var anomaly : anomalies) {
      System.out.println(Math.toDegrees(anomaly));
    }
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
