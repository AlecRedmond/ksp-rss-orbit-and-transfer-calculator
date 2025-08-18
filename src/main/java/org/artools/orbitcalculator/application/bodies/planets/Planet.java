package org.artools.orbitcalculator.application.bodies.planets;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.vector.MotionState;

@Getter
public abstract class Planet extends AstralBody {
  private final BodyType bodyType;
  private final BodyType parentBody;
  private double j2;
  private double bodyRadius;

  protected Planet() {
    super();
    parseStateVector(horizonsVectorData());
    bodyType = planetBodyType();
    parentBody = parentBodyType();
    mass = muToMass();
  }



  public Optional<BodyType> getSphereOfInfluence(){
    return Optional.ofNullable(parentBody);
  }

  public String getName(){
    return bodyType.toString();
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

  protected abstract BodyType planetBodyType();

  protected abstract BodyType parentBodyType();

  private void setHorizonsData(double[] positionArray, double[] velocityArray) {
    j2 = j2();
    Vector3D velocity1951Jan1 = kilometreToSIVector(velocityArray);
    Vector3D position1951Jan1 = kilometreToSIVector(positionArray);
    mu = muHorizons() * 1E9;
    bodyRadius = equatorialRadiusHorizons() * 1E3;
    motionState = new MotionState(velocity1951Jan1, position1951Jan1, initialEpoch());
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
  public double getMu() {
    return mu;
  }

  @Override
  public MotionState getMotionState() {
    return motionState;
  }

  @Override
  public void setMotionState(MotionState state) {
    this.motionState = state;
  }
}
