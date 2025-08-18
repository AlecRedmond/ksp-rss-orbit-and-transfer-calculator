package org.artools.orbitcalculator.application.bodies.planets;

public class Mercury extends Planet {
  @Override
  protected BodyType planetBodyType() {
    return BodyType.MERCURY;
  }

  @Override
  protected double[] horizonsDataPosition() {
    return new double[]{-4.895877361760728E+06,4.609862861539137E+07,4.190947565949773E+06};
  }

  @Override
  protected double[] horizonsDataVelocity() {
    return new double[]{-5.821310068038928E+01,-3.334358159037551E+00,5.078823433723494E+00};
  }

  @Override
  protected double j2() {
    return 0;
  }

  @Override
  protected double muHorizons() {
    return 22031.86855;
  }

  @Override
  protected double equatorialRadiusHorizons() {
    return 2440.53;
  }

  @Override
  public BodyType parentBodyType() {
    return BodyType.SUN;
  }
}
