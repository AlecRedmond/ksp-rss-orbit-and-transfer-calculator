package org.example.equations.application.vector;

import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;

import java.time.Instant;

@Data
public class MotionVectors {
  private Vector3D velocity;
  private Vector3D bodyDistance;
  private Vector3D acceleration;
  private Rotation rotationToInertial;
  private Body centralBody;
  private Instant epoch;

  public MotionVectors(Body centralBody, Vector3D velocity, Vector3D bodyDistance, Rotation rotationToInertial, Instant epoch) {
    this.centralBody = centralBody;
    this.bodyDistance = bodyDistance;
    this.velocity = velocity;
    this.rotationToInertial = rotationToInertial;
    this.epoch = epoch;
    calculateGravityAcceleration();
  }

  private void calculateGravityAcceleration() {
    var mu = centralBody.getMu();
    var radiusSquared = Math.pow(bodyDistance.getNorm(), 2);
    var radiusUnitVector = bodyDistance.normalize();
    acceleration = radiusUnitVector.scalarMultiply(mu / radiusSquared);
  }

  public Vector3D getRadius() {
    return bodyDistance.negate();
  }
}
