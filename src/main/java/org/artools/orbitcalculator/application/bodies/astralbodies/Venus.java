package org.artools.orbitcalculator.application.bodies.astralbodies;

import org.artools.orbitcalculator.application.bodies.Body;

public class Venus extends AstralBody {

  @Override
  protected String horizonsVectorData() {
    return "X = 6.672802494807968E+07 Y =-8.582044058549407E+07 Z =-5.027613098699130E+06\n"
        + " VX= 2.741737914074245E+01 VY= 2.135294831981611E+01 VZ=-1.294686974962003E+00";
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
  public Body getDefaultOrbitalFocus() {
    return Body.SUN;
  }
}
