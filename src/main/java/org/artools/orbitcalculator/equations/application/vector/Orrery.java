package org.artools.equations.application.vector;

import java.util.EnumMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.equations.application.Body;

@Data
@NoArgsConstructor
public class Orrery {
  private Map<Body, MotionVectors> map = new EnumMap<>(Body.class);

  public void putData(Body body, MotionVectors motionVectors) {
    map.put(body, motionVectors);
  }

  public MotionVectors getMotionVectors(Body body) {
    return map.getOrDefault(body, new MotionVectors());
  }
}
