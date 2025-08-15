package org.artools.orbitcalculator.application.bodies.planets;

public class Uranus extends Planet {
  @Override
  protected BodyName planetName() {
    return BodyName.URANUS;
  }

  @Override
  protected String horizonsVectorData() {
    return "X =-4.007702769867036E+08 Y = 2.796136122139190E+09 Z = 1.559876544679713E+07\n"
        + " VX=-6.792937309326550E+00 VY=-1.284745409647841E+00 VZ= 8.336433553615774E-02";
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
  public BodyName parentBody() {
    return BodyName.SUN;
  }
}
