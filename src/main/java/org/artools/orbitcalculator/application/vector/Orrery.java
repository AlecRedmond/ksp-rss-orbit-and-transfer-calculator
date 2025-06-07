package org.artools.orbitcalculator.application.vector;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.Body;
import org.artools.orbitcalculator.exceptions.NotOrbitalStateException;

@Data
@NoArgsConstructor
public class Orrery {
  private Map<Body, MotionState> bodyStateMap = new EnumMap<>(Body.class);

  public void putData(Body body, MotionState motionState) {
    bodyStateMap.put(body, motionState);
  }

  public MotionState getMotionVectors(Body body) {
    return bodyStateMap.get(body);
  }

  public OrbitalState getOrbitalVectors(Body body) throws NotOrbitalStateException {
    try {
      return (OrbitalState) bodyStateMap.get(body);
    } catch (ClassCastException e) {
      throw new NotOrbitalStateException(body);
    }
  }

  public void setMotionState(Body body, MotionState state) {
    bodyStateMap.put(body, state);
  }

  public Instant getEpoch() {
    return epochsAreEqual() ? firstInstant() : null;
  }

  private boolean epochsAreEqual() {
    Instant firstInstant = firstInstant();
    return bodyStateMap.values().stream()
        .allMatch(motionState -> motionState.getEpoch().equals(firstInstant));
  }

  private Instant firstInstant() {
    return bodyStateMap.values().stream().findFirst().map(MotionState::getEpoch).orElse(null);
  }
}
