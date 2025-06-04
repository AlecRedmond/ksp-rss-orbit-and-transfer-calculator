package org.artools.orbitcalculator.method.vector;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Body;
import org.artools.orbitcalculator.application.bodies.BodyOrbits1951;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.Orrery;

@NoArgsConstructor
@Getter
public class OrreryBuilder extends OrreryUtils {
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
    MotionState motionState =
        new MotionState(
            Body.SUN,
            Vector3D.ZERO,
            Vector3D.ZERO,
            Instant.parse("1951-01-01T00:00:00.00Z"));
    orrery.putData(Body.SUN, motionState);
  }

  private void convertAllToHelioCentric() {
    orrery.getMap().entrySet().stream()
        .filter(OrreryBuilder::isNotHelioCentric)
        .forEach(entry -> changeToSolarBodyCentredInertial(entry.getKey(), entry.getValue()));
  }

  private static boolean isNotHelioCentric(Map.Entry<Body, MotionState> entry) {
    return !entry.getKey().equals(Body.SUN) && !entry.getValue().getCentralBody().equals(Body.SUN);
  }

  private void changeToSolarBodyCentredInertial(Body body, MotionState motionState) {
    MotionState centralBodyVectors = orrery.getMotionVectors(motionState.getCentralBody());
    new MotionStateUtils()
        .changeCentralBody(motionState, centralBodyVectors)
        .ifPresent(vectors -> orrery.putData(body, vectors));
  }
}
