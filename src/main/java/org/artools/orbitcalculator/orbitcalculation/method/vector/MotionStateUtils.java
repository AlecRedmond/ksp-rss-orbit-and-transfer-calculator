package org.artools.orbitcalculator.orbitcalculation.method.vector;

import java.util.Optional;
import lombok.Getter;
import org.artools.orbitcalculator.orbitcalculation.application.vector.MotionState;

@Getter
public class MotionStateUtils {
  protected MotionState vectors = new MotionState();

  /** Returns a NEW optional */
  public Optional<MotionState> changeCentralBody(
          MotionState vectorsInOldBodyFrame, MotionState oldBodyInNewBodyFrame) {
    if (!vectorsInOldBodyFrame.getEpoch().equals(oldBodyInNewBodyFrame.getEpoch())) {
      return Optional.empty();
    }

    var centralBody = oldBodyInNewBodyFrame.getCentralBody();
    var newVelocity = vectorsInOldBodyFrame.getVelocity().add(oldBodyInNewBodyFrame.getVelocity());
    var newRadius = vectorsInOldBodyFrame.getPosition().add(oldBodyInNewBodyFrame.getPosition());
    var epoch = oldBodyInNewBodyFrame.getEpoch();
    return Optional.of(new MotionState(centralBody, newVelocity, newRadius, epoch));
  }
}
