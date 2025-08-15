package org.artools.orbitcalculator.application.bodies;

import org.artools.orbitcalculator.application.bodies.planets.BodyName;
import org.artools.orbitcalculator.application.vector.MotionState;

import java.util.Optional;

public interface AstralBody {
  double G = 6.6743015E-11;

  default double getMass() {
    return getMu() / G;
  }

  double getMu();

  MotionState getMotionState();

  void setMotionState(MotionState state);

  Optional<BodyName> getSphereOfInfluence();

  String getName();
}
