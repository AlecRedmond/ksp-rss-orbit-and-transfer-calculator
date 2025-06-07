package org.artools.orbitcalculator.method.vector;

import java.util.Arrays;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Body;

@Getter
public class OrreryBuilder extends OrreryUtils {

  public OrreryBuilder(){
      super();
  }

  public OrreryBuilder setTo1951Jan1() {
    initialisePlanetsTo1951Jan1();
    shiftToSunAtZero();
    return this;
  }

  private void initialisePlanetsTo1951Jan1() {
    Arrays.stream(Body.values())
        .filter(body -> !body.equals(Body.CRAFT))
        .forEach(this::get1951Jan1Positions);
  }

  private void shiftToSunAtZero() {
    Vector3D shiftVector = orrery.getMotionVectors(Body.SUN).getPosition().negate();
    adjustAllBy(shiftVector);
  }

  private void get1951Jan1Positions(Body body) {
    orrery.putData(body, body.get1951Jan1State());
  }

  private void adjustAllBy(Vector3D shiftVector) {
    orrery
        .getBodyStateMap()
        .values()
        .forEach(
            motionState -> motionState.setPosition(motionState.getPosition().add(shiftVector)));
  }
}
