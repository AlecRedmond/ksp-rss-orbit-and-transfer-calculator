package org.artools.orbitcalculator.application.bodies.astralbodies;

public class Sun extends AstralBody {
  @Override
  double[] velocity1951Jan1Horizons() {
    return new double[] {-2.610383151396620E-03, -9.586514629520296E-03, 5.749810730508453E-05};
  }

  @Override
  double[] position1951Jan1Horizons() {
    return new double[] {-4.301777867304494E+04, 1.174093787096878E+05, -8.299334313999803E+03};
  }

  @Override
  double muHorizons() {
    return 132712440041.93938;
  }

  @Override
  double bodyRadiusHorizons() {
    return 695700;
  }

  @Override
  protected double j2() {
    return 0;
  }
}
