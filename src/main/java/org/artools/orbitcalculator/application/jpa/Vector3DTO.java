package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Embeddable
@Data
@AllArgsConstructor
public class Vector3DTO {
  private double x;
  private double y;
  private double z;

  public Vector3DTO(Vector3D vector3D){
    this.x = vector3D.getX();
    this.y = vector3D.getY();
    this.z = vector3D.getZ();
  }
}
