package org.example.equations.method.integrator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.example.equations.application.Body;
import org.example.equations.application.vector.MotionVectors;
import org.example.equations.application.vector.Orrery;

public class OrreryIntegrator {
  private Orrery orrery;
  private double minTimeStep = 1e-6;
  private double maxTimeStep = 3600 * 24;
  private double[] y;
  private List<Body> bodies;
  private Instant epoch;

  public OrreryIntegrator(Orrery orrery) {
    this.orrery = orrery;
    initializeBodies();
    initializeStateVector();
    initializeEpoch();
  }

  public Orrery getOrrery() {
    return orrery;
  }

  private void initializeBodies() {
    bodies = orrery.getMap().keySet().stream().toList();
  }

  private void initializeStateVector() {
    y = new double[bodies.size() * 6];
    IntStream.range(0, bodies.size()).forEach(this::inputStates);
  }

  private void initializeEpoch() {
    epoch = orrery.getMap().values().stream().findFirst().get().getEpoch();
  }

  private void inputStates(int bodyIndex) {
    Body body = bodies.get(bodyIndex);
    MotionVectors mv = orrery.getMotionVectors(body);
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

  public OrreryIntegrator stepForward(double timeSecs) {
    setEpoch(timeSecs);
    integrate(timeSecs);
    writeResultsToOrrery();
    return this;
  }

  private void setEpoch(double timeSecs) {
    epoch = epoch.plus((long) timeSecs, ChronoUnit.SECONDS);
  }

  private void integrate(double timeSecs) {
    FirstOrderIntegrator integrator =
        new DormandPrince853Integrator(minTimeStep, maxTimeStep, 1e-10, 1e-10);
    NBodyODEProblem problem = new NBodyODEProblem(bodies);
    integrator.integrate(problem, 0, y, timeSecs, y);
  }

  private void writeResultsToOrrery() {
    IntStream.range(0, bodies.size()).forEach(this::writeNewBodyVectors);
  }

  private void writeNewBodyVectors(int bodyIndex) {
    int yIndex = bodyIndex * 6;
    Body body = bodies.get(bodyIndex);
    Vector3D position = new Vector3D(new double[]{y[yIndex],y[yIndex+1],y[yIndex+2]});
    Vector3D velocity = new Vector3D(new double[]{y[yIndex+3],y[yIndex+4],y[yIndex+5]});
    MotionVectors mv = orrery.getMotionVectors(body);
    mv.setEpoch(epoch);
    mv.setPosition(position);
    mv.setVelocity(velocity);
    orrery.getMap().put(body,mv);
  }
}
