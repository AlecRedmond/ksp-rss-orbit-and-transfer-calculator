package org.artools.orbitcalculator.application.bodies.astralbodies;

import org.artools.orbitcalculator.application.bodies.AstralBodies;

public class Mercury extends AstralBody {

  @Override
  protected String horizonsVectorData() {
    return "X =-4.894170451539801E+06 Y = 4.609872635824879E+07 Z = 4.190798643785544E+06\n"
        + " VX=-5.821328936093884E+01 VY=-3.332570130104314E+00 VZ= 5.078986722966221E+00";
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
  public AstralBodies getDefaultOrbitalFocus() {
    return AstralBodies.SUN;
  }
}
