package org.artools.orbitcalculator.method.integrator;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.application.bodies.AstralBodies;

public class OrreryIntegrator {
  @Getter private final Orrery orrery;
  private final double minTimeStep;
  private final double maxTimeStep;
  private double[] y;
  private List<AstralBodies> bodies;
  private Instant newEpoch;

  public OrreryIntegrator(Orrery orrery) {
    this.orrery = orrery;
    minTimeStep = 1e-6;
    maxTimeStep = 3600.0 * 24;
    initializeBodies();
    initializeStateVector();
    newEpoch = orrery.getEpoch();
  }

  private void initializeBodies() {
    bodies = orrery.getBodyStateMap().keySet().stream().toList();
  }

  private void initializeStateVector() {
    y = new double[bodies.size() * 6];
    IntStream.range(0, bodies.size()).forEach(this::inputStates);
  }

  private void inputStates(int bodyIndex) {
    AstralBodies astralBodies = bodies.get(bodyIndex);
    MotionState mv = orrery.getMotionVectors(astralBodies);
    Vector3D pos = mv.getPosition();
    Vector3D vel = mv.getVelocity();
    int yIndex = bodyIndex * 6;
    y[yIndex] = pos.getX();
    y[yIndex + 1] = pos.getY();
    y[yIndex + 2] = pos.getZ();
    y[yIndex + 3] = vel.getX();
    y[yIndex + 4] = vel.getY();
    y[yIndex + 5] = vel.getZ();
  }

  public OrreryIntegrator stepForward(Duration duration) {
    offsetEpoch(duration);
    integrate();
    writeResultsToOrrery();
    return this;
  }

  public OrreryIntegrator stepToDate(Instant epoch){
    newEpoch = epoch;
    integrate();
    writeResultsToOrrery();
    return this;
  }

  private void offsetEpoch(Duration duration) {
    newEpoch = newEpoch.plus(duration);
  }

  private void integrate() {
    try{
    Duration duration = Duration.between(orrery.getEpoch(), newEpoch);
    FirstOrderIntegrator integrator =
        new DormandPrince853Integrator(minTimeStep, maxTimeStep, 1e-10, 1e-10);
    NBodyODEProblem problem = new NBodyODEProblem(bodies);
    integrator.integrate(problem, 0, y, duration.getSeconds(), y);
    } catch (NumberIsTooSmallException ignored){
    }
  }

  private void writeResultsToOrrery() {
    IntStream.range(0, bodies.size()).forEach(this::writeNewBodyVectors);
  }

  private void writeNewBodyVectors(int bodyIndex) {
    int yIndex = bodyIndex * 6;
    AstralBodies astralBodies = bodies.get(bodyIndex);
    Vector3D position = new Vector3D(new double[] {y[yIndex], y[yIndex + 1], y[yIndex + 2]});
    Vector3D velocity = new Vector3D(new double[] {y[yIndex + 3], y[yIndex + 4], y[yIndex + 5]});
    MotionState mv = orrery.getMotionVectors(astralBodies);
    mv.setEpoch(newEpoch);
    mv.setPosition(position);
    mv.setVelocity(velocity);
    orrery.getBodyStateMap().put(astralBodies, mv);
  }
}
