package org.artools.orbitcalculator.application.bodies;

import static org.artools.orbitcalculator.constant.Constant.EARTH_STANDARD_GRAVITY;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;

@EqualsAndHashCode(callSuper = true)
@Data
public class Craft extends AstralBody {
  private String id;
  private double bodyRadius;
  private double engineISP;
  private double engineThrustNewtons;
  private double engineMassFlowRate;
  private double dryMass;
  private double remainingDeltaV;

  public Craft(
      MotionState motionState,
      String id,
      double bodyRadius,
      double engineISP,
      double engineThrustNewtons,
      double currentMass,
      double dryMass) {
    this.motionState = motionState;
    this.id = id;
    this.bodyRadius = bodyRadius;
    this.engineISP = engineISP;
    this.engineThrustNewtons = engineThrustNewtons;
    this.engineMassFlowRate = calculateMassFlowRate();
    this.mass = currentMass;
    this.dryMass = dryMass;
    this.mu = massToMu();
    this.remainingDeltaV = calculateRemainingDeltaV();
  }

  private double calculateMassFlowRate() {
    return -1 * engineThrustNewtons / (engineISP * EARTH_STANDARD_GRAVITY);
  }

  private double calculateRemainingDeltaV() {
    return (engineISP * EARTH_STANDARD_GRAVITY) * Math.log(mass / dryMass);
  }

  public void setMass(double kgMass) {
    mass = kgMass;
    mu = massToMu();
    remainingDeltaV = calculateRemainingDeltaV();
  }

  public void setSphereOfInfluence(BodyType bodyType) {
    this.sphereOfInfluence = bodyType;
  }

  @Override
  public BodyType getBodyType() {
    return null;
  }
}
