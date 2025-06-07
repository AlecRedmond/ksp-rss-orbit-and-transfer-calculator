package org.artools.orbitcalculator.application.bodies.astralbodies;

import org.artools.orbitcalculator.application.bodies.AstralBodies;

public class Uranus extends AstralBody {
  @Override
  protected String horizonsVectorData() {
    return "X =-4.007700778061882E+08 Y = 2.796136159810110E+09 Z = 1.559876300241232E+07\n"
        + " VX=-6.792937380879133E+00 VY=-1.284744925308281E+00 VZ= 8.336433921638425E-02";
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
  public AstralBodies getDefaultOrbitalFocus() {
    return AstralBodies.SUN;
  }
}
