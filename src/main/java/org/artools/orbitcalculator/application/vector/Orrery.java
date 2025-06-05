package org.artools.orbitcalculator.application.vector;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.Body;

@Data
@NoArgsConstructor
public class Orrery {
  private Map<Body, MotionState> bodyStateMap = new EnumMap<>(Body.class);

  public void putData(Body body, MotionState motionState) {
    bodyStateMap.put(body, motionState);
  }

  public MotionState getMotionVectors(Body body) {
    return bodyStateMap.getOrDefault(body, new MotionState());
  }

  public Instant getEpoch() {
    return epochsAreEqual() ? firstInstant() : null;
  }

  private Instant firstInstant() {
    return bodyStateMap.values().stream().findFirst().map(MotionState::getEpoch).orElse(null);
  }

  private boolean epochsAreEqual() {
    Instant firstInstant = firstInstant();
    return bodyStateMap.values().stream().allMatch(motionState -> motionState.getEpoch().equals(firstInstant));
  }
}
