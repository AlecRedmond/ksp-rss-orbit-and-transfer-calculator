package org.example.equations.application.vector;

import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;

@Data
public class CraftVectors {
  private Vector3D velocity;
  private Vector3D bodyDistance;
  private Vector3D acceleration;
  private Rotation rotationToInertial;
  private Body body;

  public CraftVectors(Body body, Vector3D velocity, Vector3D bodyDistance, Rotation rotationToInertial) {
    this.body = body;
    this.bodyDistance = bodyDistance;
    this.velocity = velocity;
    this.rotationToInertial = rotationToInertial;
    calculateGravityAcceleration();
  }

  private void calculateGravityAcceleration() {
    var mu = body.getMu();
    var radiusSquared = Math.pow(bodyDistance.getNorm(), 2);
    var radiusUnitVector = bodyDistance.normalize();
    acceleration = radiusUnitVector.scalarMultiply(mu / radiusSquared);
  }

  public Vector3D getRadius() {
    return bodyDistance.negate();
  }
}
