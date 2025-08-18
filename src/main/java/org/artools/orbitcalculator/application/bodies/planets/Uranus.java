package org.artools.orbitcalculator.application.bodies.planets;

public class Uranus extends Planet {
  @Override
  protected BodyType planetBodyType() {
    return BodyType.URANUS;
  }

  @Override
  protected double[] horizonsDataPosition() {
    return new double[]{-4.007702769867036E+08,2.796136122139190E+09,1.559876544679713E+07};
  }

  @Override
  protected double[] horizonsDataVelocity() {
    return new double[]{-6.792937309326550E+00,-1.284745409647841E+00,8.336433553615774E-02};
  }

  @Override
  protected double j2() {
    return 0;
  }

  @Override
  protected double muHorizons() {
    return 5793950.6103;
  }

  @Override
  protected double equatorialRadiusHorizons() {
    return 25559;
  }

  @Override
  public BodyType parentBodyType() {
    return BodyType.SUN;
  }
}
