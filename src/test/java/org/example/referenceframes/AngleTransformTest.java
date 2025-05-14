package org.example.referenceframes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.junit.jupiter.api.Test;

class AngleTransformTest {
  AngleTransform test = new AngleTransform();
  Vector3D inertialVector = new Vector3D(new double[] {1, 0, 0});
  Vector3D planarVector = new Vector3D(new double[] {0, 0, 1});
  double rightAscension = Math.toRadians(0);
  double inclination = Math.toRadians(90);
  double argumentPE = Math.toRadians(0);

  @Test
  void toOrbitalFrame() {
    Vector3D newVector =
        test.toOrbitalFrame(inertialVector, rightAscension, inclination, argumentPE);
    System.out.println(newVector);
    assertVectorsEqual(newVector, planarVector);
  }

  private void assertVectorsEqual(Vector3D vector1, Vector3D vector2) {
    assertEquals(vector1.getX(), vector2.getX(), 1e-6);
    assertEquals(vector1.getY(), vector2.getY(), 1e-6);
    assertEquals(vector1.getZ(), vector2.getZ(), 1e-6);
  }

  @Test
  void toInertialFrame() {
    getPlanarVectors(150000)
        .forEach(
            vector -> {
              System.out.println(
                  test.toInertialFrame(vector, rightAscension, inclination, argumentPE));
            });
  }

  @Test
  void intercept(){
    Orbit orbitA = new Orbit();
    orbitA.setDataFor(Kepler.KeplerEnums.RIGHT_ASCENSION,rightAscension);
    orbitA.setDataFor(Kepler.KeplerEnums.INCLINATION,inclination);
    orbitA.setDataFor(Kepler.KeplerEnums.ARGUMENT_PE,argumentPE);
    Orbit orbitB = new Orbit();
    Line line = test.intercept(orbitA,orbitB);
    System.out.println(line.getDirection());
    System.out.println(line.getDirection().negate());
  }

  public List<Vector3D> getPlanarVectors(double radius) {
    return IntStream.range(0, 361)
        .filter(i -> i % 45 == 0)
        .mapToObj(i -> getPlanarMatrix(i, radius))
        .toList();
  }

  public Vector3D getPlanarMatrix(double angle, double radius) {
    angle = Math.toRadians(angle);
    var xCoord = radius * Math.cos(angle);
    var yCoord = radius * Math.sin(angle);
    return new Vector3D(new double[] {xCoord, yCoord, 0});
  }
}
