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

  private static boolean isNotCraftOrSun(Body body) {
    return !body.equals(Body.CRAFT) && !body.equals(Body.SUN);
  }

  private static boolean isNotCraftOrSun(Map.Entry<Body, MotionState> entry) {
    return isNotCraftOrSun(entry.getKey());
  }

  private static Vector3D calculateMassPositionVector(Map.Entry<Body, MotionState> entry) {
    Vector3D position = entry.getValue().getPosition();
    double mass = entry.getKey().getMass();
    return position.scalarMultiply(mass);
  }

  private static boolean isNotHelioCentric(Map.Entry<Body, MotionState> entry) {
    return !entry.getKey().equals(Body.SUN) && !entry.getValue().getCentralBody().equals(Body.SUN);
  }

  private static Vector3D getMomentum(Map.Entry<Body, MotionState> entry) {
    return entry.getValue().getVelocity().scalarMultiply(entry.getKey().getMass());
  }

  public OrreryBuilder setTo1951Jan1() {
    initialisePlanetsTo1951Jan1();
    convertAllToHelioCentric();
    initializeSun();
    //adjustToBaryCenter();
    return this;
  }

  private void adjustToBaryCenter() {
    Vector3D centreOfMass = centreOfPlanetaryMass();
    orrery
        .getBodyStateMap()
        .values()
        .forEach(state -> state.setPosition(state.getPosition().subtract(centreOfMass)));
  }

  private Vector3D centreOfPlanetaryMass() {
    return orrery.getBodyStateMap().entrySet().stream()
        .map(OrreryBuilder::calculateMassPositionVector)
        .reduce(Vector3D.ZERO, Vector3D::add)
        .scalarMultiply(1 / getTotalMass());
  }

  private void initialisePlanetsTo1951Jan1() {
    Arrays.stream(Body.values())
        .filter(OrreryBuilder::isNotCraftOrSun)
        .forEach(this::get1951Jan1Positions);
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
    orrery.getBodyStateMap().entrySet().stream()
        .filter(OrreryBuilder::isNotHelioCentric)
        .forEach(entry -> changeToSolarBodyCentredInertial(entry.getKey(), entry.getValue()));
  }

  private void get1951Jan1Positions(Body body) {
    orrery.putData(body, BodyOrbits1951.getMotionVectors(body));
  }

  private Vector3D sunInitialVelocity() {
    double totalMass = getTotalMass();

    return orrery.getBodyStateMap().entrySet().stream()
        .filter(OrreryBuilder::isNotCraftOrSun)
        .map(OrreryBuilder::getMomentum)
        .reduce(Vector3D.ZERO, Vector3D::add)
        .scalarMultiply(-1.0 / totalMass);
  }

  private double getTotalMass() {
    double totalPlanetaryMass =
        orrery.getBodyStateMap().keySet().stream()
            .filter(OrreryBuilder::isNotCraftOrSun)
            .mapToDouble(Body::getMass)
            .sum();

    return Body.SUN.getMass() + totalPlanetaryMass;
  }

  private void changeToSolarBodyCentredInertial(Body body, MotionState motionState) {
    MotionState centralBodyVectors = orrery.getMotionVectors(motionState.getCentralBody());
    new MotionStateUtils()
        .changeCentralBody(motionState, centralBodyVectors)
        .ifPresent(vectors -> orrery.putData(body, vectors));
  }
}
