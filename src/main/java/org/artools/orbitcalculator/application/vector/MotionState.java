package org.artools.orbitcalculator.application.vector;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Body;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotionState {
  private Body centralBody;
  private Vector3D velocity;
  private Vector3D position;
  private Instant epoch;
}
