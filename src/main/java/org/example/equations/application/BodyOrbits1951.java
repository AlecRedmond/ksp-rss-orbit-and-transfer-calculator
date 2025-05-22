package org.example.equations.application;

import java.time.Instant;
import org.example.equations.application.vector.OrbitalVectors;
import org.example.equations.method.OrbitBuilder;
import org.example.equations.method.vector.MotionVectorBuilder;
import org.example.equations.method.vector.OrbitalVectorBuilder;

public class BodyOrbits1951 {
  public static OrbitalVectors getOrbitalVectors(Body body) {
    OrbitalVectors vectors = new OrbitalVectors();
    switch (body) {
      case EARTH -> vectors = earth1951();
      case SUN -> vectors = sun1951();
    }
    return vectors;
  }

  private static OrbitalVectors earth1951() {

    var sma = 1.495963847513234E+11;
    var e = 1.669646124569650E-02;
    var rightAsensionDegs = 1.346549169397265E+01;
    var inclinationDegs = 6.289856743664132E-03;
    var argumentPEDegs = 8.813625089765455E+01;
    var trueAnomalyDegs = 3.588408958011655E+02;
    return getVectors(
        sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs);
  }

  private static OrbitalVectors sun1951() {
    return new OrbitalVectors();
  }

  private static OrbitalVectors getVectors(
      double sma,
      double e,
      double rightAsensionDegs,
      double inclinationDegs,
      double argumentPEDegs,
      double trueAnomalyDegs) {
    Instant instant = Instant.parse("1951-01-01T00:00:00.00Z");
    Orbit orbit =
        new OrbitBuilder()
            .buildFromHorizonsData(sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs)
            .getOrbit();
    var motionVectorsOptional =
        new MotionVectorBuilder()
            .buildVectors(orbit, Math.toRadians(trueAnomalyDegs), instant)
            .getSOIVectors();
    if (motionVectorsOptional.isEmpty()) {
      return new OrbitalVectors();
    }
    return new OrbitalVectorBuilder().buildVectors(motionVectorsOptional.get()).getVectors();
  }
}
