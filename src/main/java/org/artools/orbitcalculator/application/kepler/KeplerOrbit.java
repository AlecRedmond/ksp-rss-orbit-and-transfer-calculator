package org.artools.orbitcalculator.application.kepler;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.method.kepler.KeplerHolds;

@Data
@NoArgsConstructor
public class KeplerOrbit {
  private Instant epoch;

  private Planet centralBody;

  private Map<KeplerElement, Double> elementsMap;

  private boolean built;

  private KeplerHolds holds;

  public KeplerOrbit(Instant epoch, Planet centralBody) {
    this.epoch = epoch;
    this.centralBody = centralBody;
    this.elementsMap = new EnumMap<>(KeplerElement.class);
  }

  public double getData(KeplerElement element) {
    return elementsMap.get(element);
  }

  public void setData(KeplerElement element, double data) {
    elementsMap.put(element, data);
  }

  public void removeEntry(KeplerElement element) {
    elementsMap.remove(element);
  }

  public boolean containsKey(KeplerElement element) {
    return elementsMap.containsKey(element);
  }
}
