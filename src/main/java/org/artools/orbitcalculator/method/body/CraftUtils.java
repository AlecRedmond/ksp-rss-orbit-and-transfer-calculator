package org.artools.orbitcalculator.method.body;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Craft;

public class CraftUtils {
  private CraftUtils() {}

  public static Craft copyOf(Craft craft) {
    return new Craft(
        craft.getMotionState(),
        craft.getId(),
        craft.getBodyRadius(),
        craft.getEngineISP(),
        craft.getEngineThrustNewtons(),
        craft.getMass(),
        craft.getDryMass());
  }

  public static Rotation rotationToVelocityCentredFrame(Craft craft) {
    Vector3D craftVelocityVector = craft.getMotionState().getVelocity();
    return new Rotation(craftVelocityVector, Vector3D.PLUS_I);
  }

  public static Vector3D getThrustVector(Craft craft, Vector3D thrustDirection) {
    return thrustDirection.normalize().scalarMultiply(craft.getEngineThrustNewtons());
  }

  public static double remainingBurnTime(Craft craft) {
    return (craft.getDryMass() - craft.getMass()) / craft.getEngineMassFlowRate();
  }

  public static double massAfterEngineBurn(Craft craft, double timeStep) {
    double newMass = craft.getMass() + craft.getEngineMassFlowRate() * timeStep;
    if (newMass < craft.getDryMass()) {
      throw new IllegalArgumentException("timeStep longer than remaining fuel");
    }
    return newMass;
  }
}
