package org.example.equations.method.vector;

import java.util.Arrays;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.equations.application.Body;
import org.example.equations.application.BodyOrbits1951;
import org.example.equations.application.vector.MotionVectors;
import org.example.equations.application.vector.Orrery;

@NoArgsConstructor
@Getter
public class OrreryBuilder {
  private final Orrery orrery = new Orrery();

  public OrreryBuilder initialPositions() {
    Arrays.stream(Body.values()).forEach(this::get1951Positions);
    convertAllToHelioCentric();
    return this;
  }

  private void get1951Positions(Body body) {
    if (body.equals(Body.SUN)) {
      return;
    }
    orrery.putData(body, BodyOrbits1951.getMotionVectors(body));
  }

  private void convertAllToHelioCentric() {
    orrery.getMap().entrySet().stream()
        .filter(OrreryBuilder::isNotHelioCentric)
        .forEach(entry -> changeToCentralBodyFocus(entry.getKey(), entry.getValue()));
  }

  private static boolean isNotHelioCentric(Map.Entry<Body, MotionVectors> entry) {
    return !entry.getKey().equals(Body.SUN) && !entry.getValue().getCentralBody().equals(Body.SUN);
  }

  private void changeToCentralBodyFocus(Body body, MotionVectors motionVectors) {
    MotionVectors centralBodyVectors = orrery.getMotionVectors(motionVectors.getCentralBody());
    new MotionVectorUtils()
        .changeCentralBody(motionVectors, centralBodyVectors)
        .ifPresent(vectors -> orrery.putData(body, vectors));
  }
}
