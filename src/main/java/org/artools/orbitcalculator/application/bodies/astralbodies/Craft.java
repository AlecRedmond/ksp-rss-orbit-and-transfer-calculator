package org.artools.orbitcalculator.application.bodies.astralbodies;

import org.artools.orbitcalculator.application.bodies.Body;

public class Craft extends AstralBody {
  @Override
  protected String horizonsVectorData() {
    return "X =-2.670489271346819E+07 Y = 1.447801334935187E+08 Z = 7.826551263824105E+03\n"
        + " VX=-2.978545710300538E+01 VY=-5.508905987834387E+00 VZ= 2.317255989376932E-04";
  }

  @Override
  double j2() {
    return 0;
  }

  @Override
  double muHorizons() {
    return 0;
  }

  @Override
  double equatorialRadiusHorizons() {
    return 0;
  }

  @Override
  public double getMass(){
    return 100;
  }

  @Override
  public double getMu(){
    return getMass() * G;
  }

  @Override
  public Body getDefaultOrbitalFocus() {
    return null;
  }
}
