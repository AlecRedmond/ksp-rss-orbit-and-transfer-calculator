package org.example.equations.application.vector;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;

import java.time.Instant;

@Data
@NoArgsConstructor
public class MotionVectors {
  private Vector3D velocity;
  private Vector3D radius;
  private Vector3D acceleration;
  private Rotation rotationToInertial;
  private Body centralBody;
  private Frame frame;
  private Instant epoch;

  public enum Frame {
    VELOCITY_FRAME, BODY_INERTIAL_FRAME;
  }

  public MotionVectors(Body centralBody, Vector3D velocity, Vector3D radius, Rotation rotationToInertial, Instant epoch, Frame frame) {
    this.centralBody = centralBody;
    this.radius = radius;
    this.velocity = velocity;
    this.rotationToInertial = rotationToInertial;
    this.epoch = epoch;
    this.frame = frame;
    calculateGravityAcceleration();
  }

  private void calculateGravityAcceleration() {
    var mu = centralBody.getMu();
    var radiusSquared = Math.pow(radius.getNorm(), 2);
    var directionToBody = radius.normalize().negate();
    acceleration = directionToBody.scalarMultiply(mu / radiusSquared);
  }

}
