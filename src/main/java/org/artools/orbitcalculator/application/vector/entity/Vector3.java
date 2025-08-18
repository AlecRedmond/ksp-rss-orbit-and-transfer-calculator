package org.artools.orbitcalculator.application.vector.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class Vector3 {
  private double x;
  private double y;
  private double z;
}
