package org.artools.orbitcalculator.application.bodies.planets;

import java.time.Instant;
import java.util.Arrays;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.vector.MotionState;

@Getter
public abstract class Planet extends AstralBody {
  private final BodyType bodyType;
  private double j2;
  private double bodyRadius;

  protected Planet() {
    super();
    parseStateVector();
    bodyType = planetBodyType();
    sphereOfInfluence = parentBodyType();
  }

  public void parseStateVector() {
    double[] positionArray = horizonsDataPosition();
    double[] velocityArray = horizonsDataVelocity();
    setHorizonsData(positionArray, velocityArray);
  }

  protected abstract BodyType planetBodyType();

  protected abstract BodyType parentBodyType();

  // These are from Barycentric (@ssb on Horizons data) @ 1951-01-01T00:00:00.00Z
  protected abstract double[] horizonsDataPosition();

  protected abstract double[] horizonsDataVelocity();

  private void setHorizonsData(double[] positionArray, double[] velocityArray) {
    j2 = j2();
    Vector3D velocity1951Jan1 = kilometreToSIVector(velocityArray);
    Vector3D position1951Jan1 = kilometreToSIVector(positionArray);
    mu = muHorizons() * 1E9;
    double mass = muToMass();
    bodyRadius = equatorialRadiusHorizons() * 1E3;
    MotionState state = new MotionState(velocity1951Jan1, position1951Jan1, initialEpoch(), mass);
    setCurrentMotionState(state);
  }

  abstract double j2();

  private Vector3D kilometreToSIVector(double[] horizonsArray) {
    return new Vector3D(Arrays.stream(horizonsArray).map(d -> d * 1E3).toArray());
  }

  // in km^3/s^2
  abstract double muHorizons();

  // in km
  abstract double equatorialRadiusHorizons();

  protected Instant initialEpoch() {
    return Instant.parse("1951-01-01T00:00:00.00Z");
  }

  @Override
  public MotionState getCurrentMotionState() {
    return currentMotionState;
  }

  @Override
  public void setCurrentMotionState(MotionState state) {
    this.currentMotionState = state;
  }

  public String getId() {
    return bodyType.toString();
  }
}
