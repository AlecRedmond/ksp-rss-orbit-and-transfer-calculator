package org.artools.orbitcalculator.application.integrator;

import static org.artools.orbitcalculator.constant.Constant.DEFAULT_CRAFT_LIFETIME;

import java.time.Instant;
import lombok.Data;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.vector.MotionState;

@Data
public class CraftItinerary implements OrreryEvent {
  private final Craft craft;
  private final Instant activationTime;
  private final Instant deactivationTime;
  private MotionState initialMotionState;
  private MotionState finalMotionState;

  public CraftItinerary(Craft craft, MotionState motionState) {
    this.craft = craft;
    this.initialMotionState = motionState;
    this.activationTime = motionState.getEpoch();
    this.deactivationTime = activationTime.plus(DEFAULT_CRAFT_LIFETIME);
  }

  @Override
  public Instant activationTime() {
    return activationTime;
  }

  @Override
  public Instant deactivationTime() {
    return deactivationTime;
  }
}
