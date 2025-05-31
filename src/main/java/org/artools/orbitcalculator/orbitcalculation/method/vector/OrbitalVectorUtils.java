package org.artools.orbitcalculator.orbitcalculation.method.vector;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.orbitcalculation.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.orbitcalculation.application.vector.OrbitalVectors;
import org.artools.orbitcalculator.orbitcalculation.method.writeableorbit.OrbitBuilder;

@Getter
@NoArgsConstructor
public class OrbitalVectorUtils {
  protected OrbitalVectors vectors = new OrbitalVectors();

  public OrbitalVectorUtils setVectors(OrbitalVectors vectors) {
    this.vectors = vectors;
    return this;
  }

  public Orbit getAsOrbit() {
    return new OrbitBuilder().buildFromVectors(vectors).getOrbit();
  }
}
