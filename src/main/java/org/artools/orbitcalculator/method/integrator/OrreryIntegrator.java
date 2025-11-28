package org.artools.orbitcalculator.method.integrator;

import static org.artools.orbitcalculator.method.integrator.OrreryIntegrator.VectorDimensionIndex.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.Orrery;

public class OrreryIntegrator {
  private static final double SCAL_ABSOLUTE_TOLERANCE = 1e-10;
  private static final double SCAL_RELATIVE_TOLERANCE = 1e-10;
  private static final double MIN_TIMESTEP_SECONDS = 1e-6;
  private static final double MAX_TIMESTEP_SECONDS = 3600.0 * 6;
  @Getter private final Orrery orrery;
  private final double[] stateVector;
  private final List<AstralBody> bodies;
  private Instant epoch;

  public OrreryIntegrator(Orrery orrery) {
    this.orrery = orrery;
    this.bodies = orrery.getAstralBodies();
    this.stateVector = initializeStateVector(bodies);
    epoch = orrery.getEpoch();
  }

  private double[] initializeStateVector(List<AstralBody> bodies) {
    double[] yInit = new double[bodies.size() * VectorDimensionIndex.getDimension()];
    IntStream.range(0, bodies.size()).forEach(this::inputStates);
    return yInit;
  }

  private void inputStates(int bodyIndex) {
    AstralBody body = bodies.get(bodyIndex);
    MotionState motionState = body.getMotionState();
    Vector3D pos = motionState.getPosition();
    Vector3D vel = motionState.getVelocity();
    int vectorIndex = bodyIndex * VectorDimensionIndex.getDimension();
    stateVector[X_POSITION.offsetIndex(vectorIndex)] = pos.getX();
    stateVector[Y_POSITION.offsetIndex(vectorIndex)] = pos.getY();
    stateVector[Z_POSITION.offsetIndex(vectorIndex)] = pos.getZ();
    stateVector[X_VELOCITY.offsetIndex(vectorIndex)] = vel.getX();
    stateVector[Y_VELOCITY.offsetIndex(vectorIndex)] = vel.getY();
    stateVector[Z_VELOCITY.offsetIndex(vectorIndex)] = vel.getZ();
  }

  public OrreryIntegrator stepForward(Duration duration) throws NumberIsTooSmallException {
    epoch = epoch.plus(duration);
    integrate();
    writeResultsToOrrery();
    return this;
  }

  private void integrate() throws NumberIsTooSmallException {
    Duration duration = Duration.between(orrery.getEpoch(), epoch);
    try {
      FirstOrderIntegrator integrator =
          new DormandPrince853Integrator(
              MIN_TIMESTEP_SECONDS,
              MAX_TIMESTEP_SECONDS,
              SCAL_ABSOLUTE_TOLERANCE,
              SCAL_RELATIVE_TOLERANCE);
      NBodyODEProblem problem = new NBodyODEProblem(bodies);
      integrator.integrate(problem, 0, stateVector, duration.getSeconds(), stateVector);
    } catch (NumberIsTooSmallException numberIsTooSmallException) {
      epoch = epoch.minus(duration);
      throw numberIsTooSmallException;
    }
  }

  private void writeResultsToOrrery() {
    IntStream.range(0, bodies.size()).forEach(this::writeNewBodyVectors);
  }

  private void writeNewBodyVectors(int bodyIndex) {
    int stateIndex = bodyIndex * VectorDimensionIndex.getDimension();
    AstralBody body = bodies.get(bodyIndex);

    Vector3D position =
        new Vector3D(
            new double[] {
              stateVector[X_POSITION.offsetIndex(stateIndex)],
              stateVector[Y_POSITION.offsetIndex(stateIndex)],
              stateVector[Z_POSITION.offsetIndex(stateIndex)]
            });

    Vector3D velocity =
        new Vector3D(
            new double[] {
              stateVector[X_VELOCITY.offsetIndex(stateIndex)],
              stateVector[Y_VELOCITY.offsetIndex(stateIndex)],
              stateVector[Z_VELOCITY.offsetIndex(stateIndex)]
            });

    body.getMotionState().setEpoch(epoch);
    body.getMotionState().setPosition(position);
    body.getMotionState().setVelocity(velocity);
  }

  public OrreryIntegrator stepToTime(Instant epoch) throws NumberIsTooSmallException {
    this.epoch = epoch;
    integrate();
    writeResultsToOrrery();
    return this;
  }

  enum VectorDimensionIndex {
    X_POSITION(0),
    Y_POSITION(1),
    Z_POSITION(2),
    X_VELOCITY(3),
    Y_VELOCITY(4),
    Z_VELOCITY(5);

    private final int offset;

    VectorDimensionIndex(int offset) {
      this.offset = offset;
    }

    public static int getDimension() {
      return values().length;
    }

    public int offsetIndex(int index) {
      return index + offset;
    }
  }
}
