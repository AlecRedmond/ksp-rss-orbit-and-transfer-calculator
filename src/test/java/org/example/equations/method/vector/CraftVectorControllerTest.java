package org.example.equations.method.vector;

import org.example.equations.application.Orbit;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CraftVectorControllerTest {
    CraftVectorController test = new CraftVectorController();

  @Test
  void buildVectors() {
      Orbit orbit = new OrbitBuilder(500e3,35786e3,90,90,270).getOrbit();
      double trueAnomaly = Math.toRadians(180);
      test.buildVectors(orbit,trueAnomaly);
      System.out.println(test.getSOIVectors());
  }
}
