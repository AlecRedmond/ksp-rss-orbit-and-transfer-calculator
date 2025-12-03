package org.artools.orbitcalculator.method.integrator;

import static org.artools.orbitcalculator.method.integrator.OrreryIntegrator.*;
import static org.artools.orbitcalculator.method.integrator.OrreryIntegrator.VectorDimensionIndex.*;

import java.util.*;
import java.util.stream.IntStream;
import lombok.Getter;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.integrator.CraftEngineBurn;
import org.artools.orbitcalculator.method.body.CraftUtils;

public class NBodyODEProblem implements FirstOrderDifferentialEquations {
  private final List<AstralBody> bodies;
  private final Map<Craft, CraftEngineBurn> craftsBurning;

  protected NBodyODEProblem(List<AstralBody> bodies, Map<Craft, CraftEngineBurn> craftsBurning) {
    this.bodies = bodies;
    this.craftsBurning = craftsBurning;
  }

  @Override
  public int getDimension() {
    return bodies.size() * VectorDimensionIndex.getDimension();
  }

  @Override
  public void computeDerivatives(double t, double[] stateVector, double[] stateDerivatives)
      throws MaxCountExceededException, DimensionMismatchException {
    IntStream.range(0, bodies.size())
        .forEach(
            bodyIndex -> {
              Vector3D acceleration = computeAcceleration(bodyIndex, stateVector);
              populateDerivatives(stateVector, stateDerivatives, bodyIndex, acceleration);
            });
  }

  private Vector3D computeAcceleration(int bodyIndex, double[] stateVector) {
    Vector3D currentPos = getPositionFromBodyIndex(bodyIndex, stateVector);
    Vector3D acceleration = thrustAcceleration(bodyIndex, stateVector);
    return IntStream.range(0, bodies.size())
        .filter(i -> i != bodyIndex)
        .mapToObj(i -> getDistance(i, stateVector, currentPos))
        .map(this::accelerationTowardsBody)
        .reduce(acceleration, Vector3D::add);
  }

  private void populateDerivatives(
      double[] stateVector, double[] stateDerivatives, int bodyIndex, Vector3D acceleration) {
    int indexZero = bodyIndex * VectorDimensionIndex.getDimension();
    Vector3D velocity = getVelocityFromBodyIndex(bodyIndex, stateVector);
    // Derivatives = Vx,Vy,Vz,Ax,Ay,Az,dm/dt
    stateDerivatives[X_POSITION.offsetIndex(indexZero)] = velocity.getX();
    stateDerivatives[Y_POSITION.offsetIndex(indexZero)] = velocity.getY();
    stateDerivatives[Z_POSITION.offsetIndex(indexZero)] = velocity.getZ();

    stateDerivatives[X_VELOCITY.offsetIndex(indexZero)] = acceleration.getX();
    stateDerivatives[Y_VELOCITY.offsetIndex(indexZero)] = acceleration.getY();
    stateDerivatives[Z_VELOCITY.offsetIndex(indexZero)] = acceleration.getZ();

    double massChangeRate =
        craftUnderThrust(bodyIndex).map(Craft::getEngineMassFlowRate).orElse(0.0);

    stateDerivatives[MASS.offsetIndex(indexZero)] = massChangeRate;
  }

  private Vector3D getPositionFromBodyIndex(int bodyIndex, double[] stateVector) {
    int stateVectorBodyIndex = VectorDimensionIndex.getDimension() * bodyIndex;
    return new Vector3D(
        new double[] {
          stateVector[X_POSITION.offsetIndex(stateVectorBodyIndex)],
          stateVector[Y_POSITION.offsetIndex(stateVectorBodyIndex)],
          stateVector[Z_POSITION.offsetIndex(stateVectorBodyIndex)]
        });
  }

  private Vector3D thrustAcceleration(int bodyIndex, double[] stateVector) {
    Optional<Craft> optionalCraft = craftUnderThrust(bodyIndex);
    if (optionalCraft.isEmpty()) {
      return Vector3D.ZERO;
    }
    Craft craft = optionalCraft.get();
    CraftEngineBurn burn = craftsBurning.get(craft);
    Vector3D thrustVector = CraftUtils.getThrustVector(craft, burn.getThrustDirection());
    double mass = stateVector[MASS.offsetIndex(bodyIndex)];
    return thrustVector.scalarMultiply(1 / mass);
  }

  private Map.Entry<AstralBody, Vector3D> getDistance(
      int bodyIndex, double[] stateVector, Vector3D currentPos) {
    AstralBody body = bodies.get(bodyIndex);
    Vector3D distance = getPositionFromBodyIndex(bodyIndex, stateVector).subtract(currentPos);
    return Map.entry(body, distance);
  }

  private Vector3D accelerationTowardsBody(Map.Entry<AstralBody, Vector3D> distanceEntry) {
    AstralBody body = distanceEntry.getKey();
    Vector3D directionUnitVector = distanceEntry.getValue().normalize();
    double distanceScalar = distanceEntry.getValue().getNorm();
    double accelerationScalar = body.getMu() / Math.pow(distanceScalar, 2);
    return directionUnitVector.scalarMultiply(accelerationScalar);
  }

  private Vector3D getVelocityFromBodyIndex(int bodyIndex, double[] stateVector) {
    int stateVectorBodyIndex = VectorDimensionIndex.getDimension() * bodyIndex;
    return new Vector3D(
        new double[] {
          stateVector[X_VELOCITY.offsetIndex(stateVectorBodyIndex)],
          stateVector[Y_VELOCITY.offsetIndex(stateVectorBodyIndex)],
          stateVector[Z_VELOCITY.offsetIndex(stateVectorBodyIndex)]
        });
  }

  private Optional<Craft> craftUnderThrust(int bodyIndex) {
    if (bodies.get(bodyIndex) instanceof Craft craft && craftsBurning.containsKey(craft)) {
      return Optional.of(craft);
    }
    return Optional.empty();
  }
}
