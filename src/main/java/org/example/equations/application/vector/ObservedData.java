package org.example.equations.application.vector;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;

@Data
@AllArgsConstructor
public class ObservedData {
  private Instant instant;
  private Vector3D radius;
  private Vector3D velocity;
  private Body body;
}
