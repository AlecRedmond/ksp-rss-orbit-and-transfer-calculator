package org.example.equations.method.vector;

import java.util.Optional;
import lombok.Getter;
import org.example.equations.application.vector.MotionVectors;

@Getter
public class MotionVectorUtils {
  protected MotionVectors vectors = new MotionVectors();

  /** Returns a NEW optional */
  public Optional<MotionVectors> changeCentralBody(
      MotionVectors motionVectors, MotionVectors centralBodyVectors) {
    if (!motionVectors.getEpoch().equals(centralBodyVectors.getEpoch())) {
      return Optional.empty();
    }

    var centralBody = centralBodyVectors.getCentralBody();
    var newVelocity = motionVectors.getVelocity().add(centralBodyVectors.getVelocity());
    var newRadius = motionVectors.getPosition().add(centralBodyVectors.getPosition());
    var epoch = centralBodyVectors.getEpoch();
    return Optional.of(new MotionVectors(centralBody, newVelocity, newRadius, epoch));
  }
}
