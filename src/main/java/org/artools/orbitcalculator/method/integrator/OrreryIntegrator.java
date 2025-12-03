package org.artools.orbitcalculator.method.integrator;

import static org.artools.orbitcalculator.method.integrator.OrreryIntegrator.VectorDimensionIndex.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;
import lombok.Getter;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.artools.orbitcalculator.application.bodies.AstralBody;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.integrator.CraftEngineBurn;
import org.artools.orbitcalculator.application.integrator.CraftItinerary;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.Orrery;

public class OrreryIntegrator {
  private static final double SCAL_ABSOLUTE_TOLERANCE = 1e-10;
  private static final double SCAL_RELATIVE_TOLERANCE = 1e-10;
  private static final double MIN_TIMESTEP_SECONDS = 1e-6;
  private static final double MAX_TIMESTEP_SECONDS = 3600.0 * 6;
  @Getter private final Orrery orrery;
  private final List<AstralBody> bodies;
  private final Map<Craft, CraftEngineBurn> craftsBurning;
  private double[] stateVector;
  private Instant epoch;

  public OrreryIntegrator(Orrery orrery) {
    this.orrery = orrery;
    this.bodies = orrery.getAstralBodies();
    this.epoch = orrery.getEpoch();
    this.craftsBurning = new HashMap<>();
    buildStateVector();
  }

  private void buildStateVector() {
    this.stateVector = new double[bodies.size() * VectorDimensionIndex.getDimension()];
    IntStream.range(0, bodies.size()).forEach(this::inputStates);
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
    stateVector[MASS.offsetIndex(vectorIndex)] = body.getMass();
  }

  public void addCraft(CraftItinerary ci, boolean forwards) {
    removeCraftFromBodiesIfPresent(ci, false);
    Craft craft = ci.getCraft();
    MotionState state = forwards ? ci.getInitialMotionState() : ci.getFinalMotionState();
    craft.setMotionState(state);
    bodies.add(craft);
    buildStateVector();
  }

  private boolean removeCraftFromBodiesIfPresent(CraftItinerary ci, boolean saveFinalState) {
    Craft craft = ci.getCraft();

    Optional<Craft> optionalCraft =
        bodies.stream()
            .filter(Craft.class::isInstance)
            .map(Craft.class::cast)
            .filter(craft::equals)
            .findAny();

    optionalCraft.ifPresent(
        c -> {
          bodies.remove(c);
          craftsBurning.remove(c);
          if (saveFinalState) ci.setFinalMotionState(c.getMotionState());
        });

    return optionalCraft.isPresent();
  }

  public void removeCraft(CraftItinerary ci, boolean forwards) {
    boolean craftRemoved = removeCraftFromBodiesIfPresent(ci, forwards);
    if (craftRemoved) buildStateVector();
  }

  public void addCraftBurn(CraftEngineBurn burn, boolean forwards) {
    Craft craft = burn.getCraft();
    if (forwards) {
      burn.setInitialMass(craft.getMass());
      burn.setInitialDeltaV(craft.getRemainingDeltaV());
    } else {
      craft.setMass(burn.getFinalMass());
    }
    craftsBurning.put(burn.getCraft(), burn);
  }

  public void removeCraftBurn(CraftEngineBurn burn, boolean forwards) {
    Craft craft = burn.getCraft();
    if (forwards) {
      burn.setFinalMass(craft.getMass());
      burn.setFinalDeltaV(craft.getRemainingDeltaV());
      burn.setExpendedDeltaV(burn.getFinalDeltaV() - burn.getInitialDeltaV());
    } else {
      craft.setMass(burn.getInitialMass());
    }
    craftsBurning.remove(burn.getCraft());
  }

  public OrreryIntegrator stepToTime(Instant epoch) throws NumberIsTooSmallException {
    Duration toStep = Duration.between(orrery.getEpoch(), epoch);
    integrate(toStep);
    this.epoch = epoch;
    writeResultsToOrrery();
    return this;
  }

  private void integrate(Duration toStep) throws NumberIsTooSmallException {
    FirstOrderIntegrator integrator =
        new DormandPrince853Integrator(
            MIN_TIMESTEP_SECONDS,
            MAX_TIMESTEP_SECONDS,
            SCAL_ABSOLUTE_TOLERANCE,
            SCAL_RELATIVE_TOLERANCE);
    NBodyODEProblem problem = new NBodyODEProblem(bodies, craftsBurning);
    integrator.integrate(problem, 0, stateVector, toStep.getSeconds(), stateVector);
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

    if (!(body instanceof Craft craft)) return;

    double mass = stateVector[MASS.offsetIndex(stateIndex)];
    craft.setMass(mass);
  }

  public OrreryIntegrator stepByDuration(Duration toStep) {
    integrate(toStep);
    this.epoch = orrery.getEpoch().plus(toStep);
    writeResultsToOrrery();
    return this;
  }

  enum VectorDimensionIndex {
    X_POSITION(0),
    Y_POSITION(1),
    Z_POSITION(2),
    X_VELOCITY(3),
    Y_VELOCITY(4),
    Z_VELOCITY(5),
    MASS(6);

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
