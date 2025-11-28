package org.artools.orbitcalculator.application.bodies;

import lombok.Getter;
import lombok.Setter;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;

@Getter
public abstract class AstralBody {
  public static final double GRAVITATIONAL_CONSTANT = 6.6743015E-11;
  protected double mass;
  protected double mu;
  @Setter protected MotionState motionState;
  @Getter protected BodyType sphereOfInfluence;

  protected AstralBody() {}

  public abstract String getName();

  protected double massToMu() {
    return GRAVITATIONAL_CONSTANT * mu;
  }

  protected double muToMass() {
    return mu / GRAVITATIONAL_CONSTANT;
  }

  public abstract BodyType getBodyType();

  public abstract double getBodyRadius();
}
