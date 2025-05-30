package org.example.equations.method.integrator;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.example.equations.application.Body;

public class NBodyODEProblem implements FirstOrderDifferentialEquations {
  private final List<Body> bodies;

  public NBodyODEProblem(List<Body> bodies) {
    this.bodies = bodies;
  }

  @Override
  public int getDimension() {
    return bodies.size() * 6; // X, Y, Z, Vx, Vy, Vz
  }

  @Override
  public void computeDerivatives(double t, double[] y, double[] yDot)
      throws MaxCountExceededException, DimensionMismatchException {
    IntStream.range(0, bodies.size()).forEach(bodyIndex -> {
      Vector3D acceleration = computeAcceleration(bodyIndex, y);
      populateYDot(y,yDot,bodyIndex,acceleration);
    });

  }

  private void populateYDot(double[] y, double[] yDot, int bodyIndex, Vector3D acceleration) {
    int indexZero = bodyIndex * 6;
    Vector3D velocity = getVelocityFromBodyIndex(bodyIndex,y);
    //Derivatives = Vx,Vy,Vz,Ax,Ay,Az
    yDot[indexZero] = velocity.getX();
    yDot[indexZero + 1] = velocity.getY();
    yDot[indexZero + 2] = velocity.getZ();

    yDot[indexZero + 3] = acceleration.getX();
    yDot[indexZero + 4] = acceleration.getY();
    yDot[indexZero + 5] = acceleration.getZ();
  }

  private Vector3D getVelocityFromBodyIndex(int bodyIndex, double[] y) {
    int indexZero = 6 * bodyIndex;
    return new Vector3D(new double[] {y[indexZero+3], y[indexZero + 4], y[indexZero + 5]});
  }

  private Vector3D getRadiusFromBodyIndex(int bodyIndex, double[] y) {
    int indexZero = 6 * bodyIndex;
    return new Vector3D(new double[] {y[indexZero], y[indexZero + 1], y[indexZero + 2]});
  }

  private Vector3D computeAcceleration(int i, double[] y) {
    Vector3D currentPos = getRadiusFromBodyIndex(i, y);
    return IntStream.range(0, bodies.size())
        .filter(bodyIndex -> bodyIndex != i)
        .mapToObj(bodyIndex -> getDistance(bodyIndex, y, currentPos))
        .map(this::accelerationTowardsBody)
        .reduce(Vector3D.ZERO, Vector3D::add);
  }

  private Map.Entry<Body, Vector3D> getDistance(int bodyIndex, double[] y, Vector3D currentPos) {
    Body body = bodies.get(bodyIndex);
    Vector3D distance = getRadiusFromBodyIndex(bodyIndex, y).subtract(currentPos);
    return Map.entry(body, distance);
  }

  private Vector3D accelerationTowardsBody(Map.Entry<Body, Vector3D> distanceEntry) {
    Body body = distanceEntry.getKey();
    Vector3D directionVector = distanceEntry.getValue().normalize();
    double distanceScalar = distanceEntry.getValue().getNorm();
    double accelerationScalar = body.getMu() / Math.pow(distanceScalar, 2);
    return directionVector.scalarMultiply(accelerationScalar);
  }

}
