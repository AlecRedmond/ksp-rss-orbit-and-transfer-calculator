package org.artools.orbitcalculator.equations.method.vector;

import java.util.Optional;
import lombok.Getter;
import org.artools.orbitcalculator.equations.application.vector.MotionVectors;

@Getter
public class MotionVectorUtils {
  protected MotionVectors vectors = new MotionVectors();

  /** Returns a NEW optional */
  public Optional<MotionVectors> changeCentralBody(
      MotionVectors vectorsInOldBodyFrame, MotionVectors oldBodyInNewBodyFrame) {
    if (!vectorsInOldBodyFrame.getEpoch().equals(oldBodyInNewBodyFrame.getEpoch())) {
      return Optional.empty();
    }

    var centralBody = oldBodyInNewBodyFrame.getCentralBody();
    var newVelocity = vectorsInOldBodyFrame.getVelocity().add(oldBodyInNewBodyFrame.getVelocity());
    var newRadius = vectorsInOldBodyFrame.getPosition().add(oldBodyInNewBodyFrame.getPosition());
    var epoch = oldBodyInNewBodyFrame.getEpoch();
    return Optional.of(new MotionVectors(centralBody, newVelocity, newRadius, epoch));
  }
}
