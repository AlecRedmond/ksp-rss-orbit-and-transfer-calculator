package org.artools.orbitcalculator.application.bodies;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;

@EqualsAndHashCode(callSuper = true)
@Data
public class Craft extends AstralBody {
  private double bodyRadius = 1.0;
  private String id;
  private double engineISP;

  public Craft(MotionState motionState, double mass, String id) {
    this.mass = mass;
    this.mu = massToMu();
    this.motionState = motionState;
    this.id = id;
  }

  public void setMass(double kgMass) {
    mass = kgMass;
    mu = massToMu();
  }

  public void setSphereOfInfluence(BodyType type) {
    this.sphereOfInfluence = type;
  }

  @Override
  public BodyType getBodyType() {
    return null;
  }
}
