package org.artools.orbitcalculator.application.bodies.astralbodies;

import java.time.Instant;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.vector.MotionState;

public abstract class AstralBody implements AstralBodyInterface {
  private double j2;
  private Vector3D velocity1951Jan1;
  private Vector3D position1951Jan1;
  private double mu;
  private double bodyRadius;

  protected AstralBody() {
    parseStateVector(horizonsVectorData());
  }

  public void parseStateVector(String input) {
    Pattern pattern = Pattern.compile("[-+]?\\d*\\.?\\d+[eE][-+]?\\d+");
    Matcher matcher = pattern.matcher(input);
    double[] numbers = new double[6];
    int count = 0;

    while (matcher.find() && count < 6) {
      numbers[count++] = Double.parseDouble(matcher.group());
    }

    if (count != 6) {
      throw new IllegalArgumentException("Expected 6 numbers, found " + count);
    }

    double[] positionArray = new double[] {numbers[0], numbers[1], numbers[2]};
    double[] velocityArray = new double[] {numbers[3], numbers[4], numbers[5]};

    setHorizonsData(positionArray, velocityArray);
  }

  // These are from Barycentric (@ssb on Horizons data)
  abstract String horizonsVectorData();

  private void setHorizonsData(double[] positionArray, double[] velocityArray) {
    j2 = j2();
    velocity1951Jan1 = kilometreToSIVector(velocityArray);
    position1951Jan1 = kilometreToSIVector(positionArray);
    mu = muHorizons() * 1E9;
    bodyRadius = equatorialRadiusHorizons() * 1E3;
  }

  abstract double j2();

  private Vector3D kilometreToSIVector(double[] horizonsArray) {
    return new Vector3D(Arrays.stream(horizonsArray).map(d -> d * 1E3).toArray());
  }

  // in km^3/s^2
  abstract double muHorizons();

  // in km
  abstract double equatorialRadiusHorizons();

  @Override
  public double getMu() {
    return mu;
  }

  @Override
  public double getJ2() {
    return j2;
  }

  @Override
  public double getBodyRadius() {
    return bodyRadius;
  }

  @Override
  public MotionState getMotionState1951Jan1() {
    return new MotionState(
        velocity1951Jan1, position1951Jan1, Instant.parse("1951-01-01T00:00:00.00Z"));
  }
}
