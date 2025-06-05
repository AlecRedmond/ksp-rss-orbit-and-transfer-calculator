package org.artools.orbitcalculator.application.bodies.astralbodies;

import java.time.Instant;
import java.util.Arrays;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Body;
import org.artools.orbitcalculator.application.vector.MotionState;

public abstract class AstralBody implements AstralBodyInterface {
  private double j2;
  private Vector3D velocity1951Jan1;
  private Vector3D position1951Jan1;
  private double mu;
  private double bodyRadius;

  protected AstralBody() {
    setHorizonsData();
  }

  private void setHorizonsData() {
    j2 = j2();
    velocity1951Jan1 = kilometreToSIVector(velocity1951Jan1Horizons());
    position1951Jan1 = kilometreToSIVector(position1951Jan1Horizons());
    mu = muHorizons() * 10e9;
    bodyRadius = bodyRadiusHorizons() * 10e3;
  }

  abstract double j2();

  private Vector3D kilometreToSIVector(double[] horizonsArray) {
    return new Vector3D(Arrays.stream(horizonsArray).map(d -> d * 10e3).toArray());
  }

  // These are from Barycentric (@ssb on Horizons data)
  abstract double[] velocity1951Jan1Horizons();

  abstract double[] position1951Jan1Horizons();

  // in km^3/s^2
  abstract double muHorizons();

  // in km
  abstract double bodyRadiusHorizons();

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
        Body.SUN, velocity1951Jan1, position1951Jan1, Instant.parse("1951-01-01T00:00:00.00Z"));
  }
}
