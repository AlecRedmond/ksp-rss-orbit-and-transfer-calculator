package org.artools.orbitcalculator.orbitcalculation.method.vector;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.orbitcalculation.application.vector.OrbitalState;
import org.artools.orbitcalculator.orbitcalculation.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.orbitcalculation.method.writeableorbit.OrbitBuilder;

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
