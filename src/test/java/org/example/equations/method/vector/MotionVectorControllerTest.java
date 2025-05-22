package org.example.equations.method.vector;

import org.example.equations.application.Orbit;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

class MotionVectorControllerTest {
    MotionVectorController test = new MotionVectorController();

  @Test
  void buildVectors() {
      Orbit orbit = new OrbitBuilder(500e3,35786e3,0,0,0).getOrbit();
      double trueAnomaly = Math.toRadians(0);
      test.buildVectors(orbit,trueAnomaly);
      System.out.println(test.getSOIVectors());
      var firstRadius = test.getSOIVectors().get().getBodyDistance();
      var firstVelocity = test.getSOIVectors().get().getVelocity();
      System.out.println(firstRadius);
      System.out.println(firstVelocity);
  }
}
