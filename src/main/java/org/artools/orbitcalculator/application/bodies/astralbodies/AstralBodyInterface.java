package org.artools.orbitcalculator.application.bodies.astralbodies;

import org.artools.orbitcalculator.application.bodies.AstralBodies;
import org.artools.orbitcalculator.application.vector.MotionState;

public interface AstralBodyInterface {
  double G = 6.6743015E-11;

  default double getMass() {
    return getMu() / G;
  }
  double getMu();

  double getJ2();

  double getBodyRadius();

  MotionState getMotionState1951Jan1();

  AstralBodies getDefaultOrbitalFocus();
}
