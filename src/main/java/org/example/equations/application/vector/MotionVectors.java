package org.example.equations.application.vector;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;

@Data
@NoArgsConstructor
public class MotionVectors {
  private Vector3D velocity;
  private Vector3D position;
  private Vector3D acceleration;
  private Rotation rotationToInertial;
  private Body centralBody;
  private Frame frame;
  private Instant epoch;

  public MotionVectors(
      Body centralBody,
      Vector3D velocity,
      Vector3D position,
      Rotation rotationToInertial,
      Instant epoch,
      Frame frame) {
    this.centralBody = centralBody;
    this.position = position;
    this.velocity = velocity;
    this.rotationToInertial = rotationToInertial;
    this.epoch = epoch;
    this.frame = frame;
    calculateGravityAcceleration();
  }

  private void calculateGravityAcceleration() {
    try {
      var mu = centralBody.getMu();
      var radiusSquared = Math.pow(position.getNorm(), 2);
      var directionToBody = position.normalize().negate();
      acceleration = directionToBody.scalarMultiply(mu / radiusSquared);
    } catch (MathArithmeticException e) {
      acceleration = Vector3D.ZERO;
    }
  }

  public enum Frame {
    VELOCITY_FRAME,
    BODY_INERTIAL_FRAME;
  }
}
