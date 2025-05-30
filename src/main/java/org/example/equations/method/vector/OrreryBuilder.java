package org.example.equations.method.vector;

import static org.example.equations.application.Body.*;
import static org.example.equations.application.vector.MotionVectors.Frame.BODY_INERTIAL_FRAME;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;
import org.example.equations.application.BodyOrbits1951;
import org.example.equations.application.vector.MotionVectors;
import org.example.equations.application.vector.Orrery;

@NoArgsConstructor
@Getter
public class OrreryBuilder {
  private final Orrery orrery = new Orrery();

  public OrreryBuilder setTo1951Jan1() {
    Arrays.stream(Body.values())
        .filter(body -> !body.equals(CRAFT) && !body.equals(SUN))
        .forEach(this::get1951Jan1Positions);
    initializeSun();
    convertAllToHelioCentric();
    return this;
  }

  private void get1951Jan1Positions(Body body) {
    orrery.putData(body, BodyOrbits1951.getMotionVectors(body));
  }

  private void initializeSun() {
    MotionVectors motionVectors =
        new MotionVectors(
            SUN,
            Vector3D.ZERO,
            Vector3D.ZERO,
            new Rotation(Vector3D.PLUS_K, 0, RotationConvention.FRAME_TRANSFORM),
            Instant.parse("1951-01-01T00:00:00.00Z"),
            BODY_INERTIAL_FRAME);
    orrery.putData(SUN, motionVectors);
  }

  private void convertAllToHelioCentric() {
    orrery.getMap().entrySet().stream()
        .filter(OrreryBuilder::isNotHelioCentric)
        .forEach(entry -> changeToCentralBodyFocus(entry.getKey(), entry.getValue()));
  }

  private static boolean isNotHelioCentric(Map.Entry<Body, MotionVectors> entry) {
    return !entry.getKey().equals(SUN) && !entry.getValue().getCentralBody().equals(SUN);
  }

  private void changeToCentralBodyFocus(Body body, MotionVectors motionVectors) {
    MotionVectors centralBodyVectors = orrery.getMotionVectors(motionVectors.getCentralBody());
    new MotionVectorUtils()
        .changeCentralBody(motionVectors, centralBodyVectors)
        .ifPresent(vectors -> orrery.putData(body, vectors));
  }
}
