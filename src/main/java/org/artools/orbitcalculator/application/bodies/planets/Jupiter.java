package org.artools.orbitcalculator.application.bodies.planets;

public class Jupiter extends Planet {

  @Override
  protected double[] horizonsDataPosition() {
    return new double[]{7.181716522793443E+08,-1.965436017022419E+08,-1.529523622004643E+07};
  }

  @Override
  protected double[] horizonsDataVelocity() {
    return new double[]{3.290023171949559E+00, 1.322568784338261E+01, -1.281302905289552E-01};
  }

  @Override
  protected BodyType planetBodyType() {
    return BodyType.JUPITER;
  }

  @Override
  public BodyType parentBodyType() {
    return BodyType.SUN;
  }

  @Override
  protected double j2() {
    return 0;
  }

  @Override
  protected double muHorizons() {
    return 126686531.900;
  }

  @Override
  protected double equatorialRadiusHorizons() {
    return 71492;
  }
}
