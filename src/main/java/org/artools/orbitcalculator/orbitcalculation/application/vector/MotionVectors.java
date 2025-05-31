package org.artools.orbitcalculator.orbitcalculation.application.vector;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.orbitcalculation.application.Body;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotionVectors {
  private Body centralBody;
  private Vector3D velocity;
  private Vector3D position;
  private Instant epoch;
}
