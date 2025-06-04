package org.artools.orbitcalculator.orbitcalculation.application.vector;

import java.util.EnumMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.orbitcalculation.application.Body;

@Data
@NoArgsConstructor
public class Orrery {
  private Map<Body, MotionState> map = new EnumMap<>(Body.class);

  public void putData(Body body, MotionState motionState) {
    map.put(body, motionState);
  }

  public MotionState getMotionVectors(Body body) {
    return map.getOrDefault(body, new MotionState());
  }
}
