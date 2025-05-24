package org.example.equations.application;

import java.time.Instant;
import org.example.equations.application.vector.OrbitalVectors;
import org.example.equations.method.OrbitBuilder;
import org.example.equations.method.vector.MotionVectorBuilder;
import org.example.equations.method.vector.OrbitalVectorBuilder;

public class BodyOrbits1951 {

  private BodyOrbits1951() {}

  public static OrbitalVectors getOrbitalVectors(Body body) {
    OrbitalVectors vectors = new OrbitalVectors();
    switch (body) {
      case JUPITER -> vectors = jupiter1951();
      case SATURN -> vectors = saturn1951();
      case NEPTUNE -> vectors = neptune1951();
      case URANUS -> vectors = uranus1951();
      case VENUS -> vectors = venus1951();
      case MARS -> vectors = mars1951();
      case MERCURY -> vectors = mercury1951();
      case MOON -> vectors = moon1951();
      case EARTH -> vectors = earth1951();
      case SUN -> vectors = sun1951();
    }
    return vectors;
  }

  private static OrbitalVectors moon1951() {
    var sma = 3.799350804775866E+05; // A
    var e = 5.520638951835218E-02; // EC
    var rightAsensionDegs = 3.525773788808153E+02; // OM
    var inclinationDegs = 5.062074376662730E+00; // IN
    var argumentPEDegs = 2.844864400832248E+02; // W
    var trueAnomalyDegs = 2.707284399988295E+02; // TA
    return getVectors(
            sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.EARTH);
  }

  private static OrbitalVectors mercury1951() {
    var sma = 5.790899028576464E+07; // A
    var e = 2.056177912766275E-01; // EC
    var rightAsensionDegs = 4.839193279902154E+01; // OM
    var inclinationDegs = 7.007838625011914E+00; // IN
    var argumentPEDegs = 2.898861975116913E+01; // W
    var trueAnomalyDegs = 1.885586466927809E+01; // TA
    return getVectors(
            sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.SUN);
  }

  private static OrbitalVectors jupiter1951() {
    var sma = 7.783124763169246E+08; // A
    var e = 6.808524916462147E-03; // EC
    var rightAsensionDegs = 1.003930097175018E+02; // OM
    var inclinationDegs = 1.305194877558785E+00; // IN
    var argumentPEDegs = 2.740017019807200E+02; // W
    var trueAnomalyDegs = 3.302977303060965E+02; // TA
    return getVectors(
        sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.SUN);
  }

  private static OrbitalVectors saturn1951() {
    var sma = 1.424215956312398E+09; // A
    var e = 1.669646124569650E-02; // EC
    var rightAsensionDegs = 1.138795873644347E+02; // OM
    var inclinationDegs = 2.487832133233655E+00; // IN
    var argumentPEDegs = 3.367240499154383E+02; // W
    var trueAnomalyDegs = 8.643683415058248E+01; // TA
    return getVectors(
        sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.SUN);
  }

  private static OrbitalVectors neptune1951() {
    var sma = 4.490035724335194E+09; // A
    var e = 9.869019515608365E-03; // EC
    var rightAsensionDegs = 1.318523583815618E+02; // OM
    var inclinationDegs = 1.770720399621819E+00; // IN
    var argumentPEDegs = 2.625642732369856E+02; // W
    var trueAnomalyDegs = 1.638637400667446E+02; // TA
    return getVectors(
            sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.SUN);

  }

  private static OrbitalVectors uranus1951() {
    var sma = 2.870791387145711E+09; // A
    var e = 4.608379864433731E-02; // EC
    var rightAsensionDegs = 7.397985519737271E+01; // OM
    var inclinationDegs = 7.729659221253420E-01; // IN
    var argumentPEDegs = 9.625324677001413E+01; // W
    var trueAnomalyDegs = 2.879249694320588E+02; // TA
    return getVectors(
            sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.SUN);

  }

  private static OrbitalVectors venus1951() {

    var sma = 1.082089074269984E+08; // A
    var e = 6.808524916462147E-03; // EC
    var rightAsensionDegs = 7.681713587018103E+01; // OM
    var inclinationDegs = 3.394959349560003E+00; // IN
    var argumentPEDegs = 5.440987445502214E+01; // W
    var trueAnomalyDegs = 1.766682885667086E+02; // TA
    return getVectors(
        sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.SUN);
  }

  private static OrbitalVectors mars1951() {
    var sma = 2.279508650650513E+08; // A
    var e = 9.343352557950071E-02; // EC
    var rightAsensionDegs = 4.970153890842160E+01; // OM
    var inclinationDegs = 1.853729173935577E+00; // IN
    var argumentPEDegs = 2.861546939259333E+02; // W
    var trueAnomalyDegs = 8.651507440274161E-01; // TA
    return getVectors(
            sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.SUN);
  }

  private static OrbitalVectors earth1951() {

    var sma = 1.495963847513234E+08; // A
    var e = 1.669646124569650E-02; // EC
    var rightAsensionDegs = 1.346549169397265E+01; // OM
    var inclinationDegs = 6.289856743664132E-03; // IN
    var argumentPEDegs = 8.813625089765455E+01; // W
    var trueAnomalyDegs = 3.588408958011655E+02; // TA
    return getVectors(
        sma, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, trueAnomalyDegs, Body.SUN);
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
      double trueAnomalyDegs,
      Body centralBody) {
    Instant instant = Instant.parse("1951-01-01T00:00:00.00Z");
    Orbit orbit =
        new OrbitBuilder()
            .buildFromHorizonsData(
                sma * 1e3, e, rightAsensionDegs, inclinationDegs, argumentPEDegs, centralBody)
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
