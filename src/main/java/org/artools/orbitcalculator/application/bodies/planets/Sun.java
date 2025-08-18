package org.artools.orbitcalculator.application.bodies.planets;

public class Sun extends Planet {
  @Override
  protected BodyType planetBodyType() {
    return BodyType.SUN;
  }

  @Override
  protected String horizonsVectorData() {
    return "X =-4.301777867304494E+04 Y = 1.174093787096878E+05 Z =-8.299334313999803E+03\n"
        + " VX=-2.610383151396620E-03 VY=-9.586514629520296E-03 VZ= 5.749810730508453E-05";
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
