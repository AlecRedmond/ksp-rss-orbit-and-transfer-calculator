package org.artools.orbitcalculator.application.integrator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.method.vector.MotionStateUtils;

@Data
public class CraftItinerary implements OrreryEvent {
  private final Craft craft;
  private final List<MotionState> snapshots;

  public CraftItinerary(Craft craft) {
    this.craft = craft;
    this.snapshots = new ArrayList<>();
    this.snapshots.add(MotionStateUtils.copyOf(craft.getCurrentMotionState()));
  }

  public Instant getLastSnapshotTime() {
    return snapshots.getFirst().getEpoch();
  }

  @Override
  public Instant getInitializationTime() {
    return snapshots.getFirst().getEpoch();
  }

  @Override
  public Instant deactivationTime() {
    return getInitializationTime().plus(100, ChronoUnit.YEARS);
  }
}
