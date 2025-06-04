package org.artools.orbitcalculator.method.vector;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.method.writeableorbit.OrbitBuilder;

@Getter
@NoArgsConstructor
public class OrbitalStateUtils {
  protected OrbitalState vectors = new OrbitalState();

  public OrbitalStateUtils setVectors(OrbitalState vectors) {
    this.vectors = vectors;
    return this;
  }

  public Orbit getAsOrbit() {
    return new OrbitBuilder().buildFromVectors(vectors).getOrbit();
  }
}
