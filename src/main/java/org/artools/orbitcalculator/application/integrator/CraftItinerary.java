package org.artools.orbitcalculator.application.integrator;

import static org.artools.orbitcalculator.constant.Constant.DEFAULT_CRAFT_LIFETIME;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.vector.MotionState;

@Data
public class CraftItinerary implements OrreryEvent {
  private final Craft craft;
  private final MotionState initialMotionState;
  private final Instant activationTime;
  private final Instant deactivationTime;
  private List<CraftEngineBurn> burns;

  public CraftItinerary(Craft craft, MotionState motionState) {
    this.craft = craft;
    this.initialMotionState = motionState;
    this.activationTime = motionState.getEpoch();
    this.deactivationTime = activationTime.plus(DEFAULT_CRAFT_LIFETIME);
    this.burns = new ArrayList<>();
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
