package org.artools.orbitcalculator.application.bodies;

import static org.artools.orbitcalculator.constant.Constant.GRAVITATIONAL_CONSTANT;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.method.vector.MotionStateUtils;

@Getter
public abstract class AstralBody {
  protected double mu;
  @Getter protected MotionState currentMotionState;
  protected List<MotionState> snapshots;
  protected BodyType sphereOfInfluence;

  protected AstralBody() {
    snapshots = new ArrayList<>();
  }

  public Instant getInitializationTime() {
    return snapshots.getFirst().getEpoch();
  }

  public abstract String getId();

  public abstract BodyType getBodyType();

  public abstract double getBodyRadius();

  public Instant rollBackStates(Instant rollBackTime) {
    if (rollBackTime.isBefore(getInitializationTime())) {
      return rollBackStates(getInitializationTime());
    }
    List<MotionState> toRemove =
        snapshots.stream()
            .filter(motionState -> motionState.getEpoch().isAfter(rollBackTime))
            .toList();

    snapshots.removeAll(toRemove);

    currentMotionState = MotionStateUtils.copyOf(snapshots.getLast());

    return getCurrentEpoch();
  }

  public void setCurrentMotionState(MotionState state) {
    if (state.getEpoch().isBefore(getCurrentEpoch())) {
      // TODO - Make a custom exception here
      throw new IllegalArgumentException(
          "Attempted to add a state prior to the current epoch in AstralBody %s".formatted(this));
    }
    currentMotionState = state;
    snapshots.add(MotionStateUtils.copyOf(state));
  }

  public Instant getCurrentEpoch() {
    return currentMotionState.getEpoch();
  }

  protected double massToMu() {
    return GRAVITATIONAL_CONSTANT * getMass();
  }

  public double getMass() {
    return getCurrentMotionState().getMass();
  }

  protected double muToMass() {
    return mu / GRAVITATIONAL_CONSTANT;
  }
}
