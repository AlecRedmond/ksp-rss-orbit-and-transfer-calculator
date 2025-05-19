package org.example.equations.application.vector;

import java.util.*;

import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;

@Data
public class CraftVectors {
  private Vector3D velocity;
  private Vector3D radius;
  private Vector3D acceleration;
  private Rotation rotationToInertial;
  private Body body;

  public CraftVectors(Body body, Vector3D velocity, Vector3D radius, Rotation rotationToInertial) {
    this.body = body;
    this.radius = radius;
    this.velocity = velocity;
    this.rotationToInertial = rotationToInertial;
    calculateGravityAcceleration();
  }

  private void calculateGravityAcceleration() {
    var mu = body.getMu();
    var radiusSquared = Math.pow(radius.getNorm(), 2);
    var radiusUnitVector = radius.normalize();
    acceleration = radiusUnitVector.scalarMultiply(mu / radiusSquared);
  }
}
