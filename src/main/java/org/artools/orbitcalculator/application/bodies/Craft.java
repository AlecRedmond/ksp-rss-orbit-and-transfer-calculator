package org.artools.orbitcalculator.application.bodies;

import lombok.Getter;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;

@Getter
public class Craft extends AstralBody {
  public final String name;

  public Craft(MotionState motionState, double mass, String name) {
    this.mass = mass;
    this.mu = massToMu();
    this.motionState = motionState;
    this.name = name;
  }

  public void setMass(double kgMass) {
    mass = kgMass;
    mu = massToMu();
  }

  public void setSphereOfInfluence(BodyType type) {
    this.sphereOfInfluence = type;
  }
}
