package org.example.equations.method.vector;

import org.example.equations.application.Orbit;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class MotionVectorBuilderTest {
    MotionVectorBuilder test = new MotionVectorBuilder();
    Instant epoch = Instant.parse("1951-01-01T00:00:00.00Z");

  @Test
  void buildVectors() {
      Orbit orbit = new OrbitBuilder(500e3,35786e3,0,0,0).getOrbit();
      double trueAnomaly = Math.toRadians(0);
      test.buildVectors(orbit,trueAnomaly,epoch);
      System.out.println(test.getSOIVectors());
      var firstRadius = test.getSOIVectors().get().getBodyDistance();
      var firstVelocity = test.getSOIVectors().get().getVelocity();
      System.out.println(firstRadius);
      System.out.println(firstVelocity);
  }
}
