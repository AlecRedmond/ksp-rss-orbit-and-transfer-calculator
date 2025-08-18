package org.artools.orbitcalculator.application.bodies.planets;

public class Sun extends Planet {
  @Override
  protected BodyType planetBodyType() {
    return BodyType.SUN;
  }

  @Override
  protected double[] horizonsDataPosition() {
    return new double[]{-4.301785521384158E+04,1.174090976166967E+05,-8.299332628059026E+03};
  }

  @Override
  protected double[] horizonsDataVelocity() {
    return new double[]{-2.610376871003066E-03,-9.586516161212495E-03,5.749798102632365E-05};
  }

  @Override
  protected double j2() {
    return 0;
  }

  @Override
  protected double muHorizons() {
    return 132712440041.93938;
  }

  @Override
  protected double equatorialRadiusHorizons() {
    return 695700;
  }

  @Override
  public BodyType parentBodyType() {
    return null;
  }
}
