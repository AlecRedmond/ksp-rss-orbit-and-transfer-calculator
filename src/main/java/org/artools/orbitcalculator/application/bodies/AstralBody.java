package org.artools.orbitcalculator.application.bodies;

import org.artools.orbitcalculator.application.vector.MotionState;

public interface AstralBody {
  double G = 6.6743015E-11;

  default double getMass() {
    return getMu() / G;
  }

  double getMu();

  MotionState getMotionState();

  BodyType getBodyType();

  void setMotionState(MotionState state);
}
