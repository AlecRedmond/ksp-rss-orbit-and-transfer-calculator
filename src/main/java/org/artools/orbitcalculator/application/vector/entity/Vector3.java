package org.artools.orbitcalculator.application.vector.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Embeddable
@Data
@AllArgsConstructor
public class Vector3 {
  private double x;
  private double y;
  private double z;

  public Vector3(Vector3D vector3D){
    this.x = vector3D.getX();
    this.y = vector3D.getY();
    this.z = vector3D.getZ();
  }
}
