package org.artools.equations.method.vector;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artools.equations.application.Orbit;
import org.artools.equations.application.vector.OrbitalVectors;
import org.artools.equations.method.OrbitBuilder;

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
