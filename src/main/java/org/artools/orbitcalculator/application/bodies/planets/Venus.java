package org.artools.orbitcalculator.application.bodies.planets;

public class Venus extends Planet {
  @Override
  protected BodyType planetBodyType() {
    return BodyType.VENUS;
  }

  @Override
  protected double[] horizonsDataPosition() {
    return new double[]{6.672882886947885E+07,-8.581981447681817E+07,-5.027651060909204E+06};
  }

  @Override
  protected double[] horizonsDataVelocity() {
    return new double[]{2.741717820369225E+01,2.135320694286949E+01,-1.294671869668157E+00};
  }

  @Override
  protected double j2() {
    return 0;
  }

  @Override
  protected double muHorizons() {
    return 324858.592;
  }

  @Override
  protected double equatorialRadiusHorizons() {
    return 6051.893;
  }

  @Override
  public BodyType parentBodyType() {
    return BodyType.SUN;
  }
}
