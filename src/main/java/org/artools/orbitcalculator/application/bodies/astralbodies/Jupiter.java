package org.artools.orbitcalculator.application.bodies.astralbodies;

import org.artools.orbitcalculator.application.bodies.Body;

public class Jupiter extends AstralBody {
  @Override
  protected String horizonsVectorData() {
    return "X = 7.181715558101405E+08 Y =-1.965439895019584E+08 Z =-1.529523246304591E+07\n"
        + " VX= 3.290030663487953E+00 VY= 1.322568640142670E+01 VZ=-1.281304086408337E-01";
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

  @Override
  public Body getDefaultOrbitalFocus() {
    return Body.SUN;
  }
}
