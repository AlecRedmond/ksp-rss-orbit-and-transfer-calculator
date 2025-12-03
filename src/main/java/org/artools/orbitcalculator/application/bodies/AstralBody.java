package org.artools.orbitcalculator.application.bodies;

import lombok.Getter;
import lombok.Setter;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;

import static org.artools.orbitcalculator.constant.Constant.GRAVITATIONAL_CONSTANT;

@Getter
public abstract class AstralBody {
  protected double mass;
  protected double mu;
  @Setter protected MotionState motionState;
  @Getter protected BodyType sphereOfInfluence;

  protected AstralBody() {}

  public abstract String getId();

  protected double massToMu() {
    return GRAVITATIONAL_CONSTANT * mu;
  }

  protected double muToMass() {
    return mu / GRAVITATIONAL_CONSTANT;
  }

  public abstract BodyType getBodyType();

  public abstract double getBodyRadius();
}
