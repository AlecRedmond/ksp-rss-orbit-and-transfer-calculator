package org.artools.orbitcalculator.equations.method.vector;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.equations.application.Body;
import org.artools.orbitcalculator.equations.application.BodyOrbits1951;
import org.artools.orbitcalculator.equations.application.vector.MotionVectors;
import org.artools.orbitcalculator.equations.application.vector.Orrery;

@NoArgsConstructor
@Getter
public class OrreryBuilder {
  private final Orrery orrery = new Orrery();

  public OrreryBuilder setTo1951Jan1() {
    initialisePlanetsTo1951Jan1();
    initializeSun();
    convertAllToHelioCentric();
    return this;
  }

  private void initialisePlanetsTo1951Jan1() {
    Arrays.stream(Body.values())
        .filter(body -> !body.equals(Body.CRAFT) && !body.equals(Body.SUN))
        .forEach(this::get1951Jan1Positions);
  }

  private void get1951Jan1Positions(Body body) {
    orrery.putData(body, BodyOrbits1951.getMotionVectors(body));
  }

  private void initializeSun() {
    MotionVectors motionVectors =
        new MotionVectors(
            Body.SUN,
            Vector3D.ZERO,
            Vector3D.ZERO,
            Instant.parse("1951-01-01T00:00:00.00Z"));
    orrery.putData(Body.SUN, motionVectors);
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
