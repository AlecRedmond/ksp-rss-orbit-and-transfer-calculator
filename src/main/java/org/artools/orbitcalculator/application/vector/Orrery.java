package org.artools.orbitcalculator.application.vector;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.AstralBodies;
import org.artools.orbitcalculator.exceptions.NotOrbitalStateException;

@Data
@NoArgsConstructor
public class Orrery {
  private Map<AstralBodies, MotionState> bodyStateMap = new EnumMap<>(AstralBodies.class);

  public void putData(AstralBodies astralBodies, MotionState motionState) {
    bodyStateMap.put(astralBodies, motionState);
  }

  public MotionState getMotionVectors(AstralBodies astralBodies) {
    return bodyStateMap.get(astralBodies);
  }

  public OrbitalState getOrbitalVectors(AstralBodies astralBodies) throws NotOrbitalStateException {
    try {
      return (OrbitalState) bodyStateMap.get(astralBodies);
    } catch (ClassCastException e) {
      throw new NotOrbitalStateException(astralBodies);
    }
  }

  public void setMotionState(AstralBodies astralBodies, MotionState state) {
    bodyStateMap.put(astralBodies, state);
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
