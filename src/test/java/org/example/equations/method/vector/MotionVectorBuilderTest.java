package org.example.equations.method.vector;

import org.example.equations.application.Orbit;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.example.equations.application.vector.MotionVectors.Frame.VELOCITY_FRAME;

class MotionVectorBuilderTest {
    MotionVectorBuilder test = new MotionVectorBuilder();
    Instant epoch = Instant.parse("1951-01-01T00:00:00.00Z");

  @Test
  void buildVectors() {
      Orbit orbit = new OrbitBuilder(500e3,35786e3,90,64.3,270).getOrbit();
      double trueAnomaly = Math.toRadians(15);
      test.buildVectors(orbit,trueAnomaly,epoch, VELOCITY_FRAME);
      System.out.println(test.getSOIVectors());
      var firstRadius = test.getSOIVectors().get().getRadius();
      var firstVelocity = test.getSOIVectors().get().getVelocity();
      System.out.println(firstRadius);
      System.out.println(firstVelocity);
  }
}
