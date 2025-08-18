package org.artools.orbitcalculator.application.vector;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MotionState {
  protected Vector3D velocity;
  protected Vector3D position;
  protected Instant epoch;
}
