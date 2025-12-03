package org.artools.orbitcalculator.application.integrator;

import java.time.Instant;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.vector.MotionState;

@Data
public class CraftEngineBurn implements OrreryEvent {
  private final Craft craft;
  private Vector3D thrustDirection;
  private Instant burnStart;
  private Instant burnEnd;
  private MotionState burnStartState;
  private MotionState burnEndState;
  private double startMass;
  private double endMass;
  private double expendedDeltaV;

  public CraftEngineBurn(Craft craft) {
    this.craft = craft;
  }

  @Override
  public Instant activationTime() {
    return burnStart;
  }

  @Override
  public Instant deactivationTime() {
    return burnEnd;
  }
}
