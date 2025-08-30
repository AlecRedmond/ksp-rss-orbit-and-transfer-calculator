package org.artools.orbitcalculator.application.kepler;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import lombok.Data;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.bodies.planets.Planet;

@Data
public class KeplerOrbit {
  private Instant epoch;
  private Planet centralBody;
  private Map<KeplerElements, Double> elementsMap;

  public KeplerOrbit(Instant epoch, Planet centralBody) {
    this.epoch = epoch;
    this.centralBody = centralBody;
    this.elementsMap = new EnumMap<>(KeplerElements.class);
  }

  public Double getData(KeplerElements element) {
    return elementsMap.get(element);
  }

  public void setData(KeplerElements element,double data){
    elementsMap.put(element,data);
  }

  public void removeEntry(KeplerElements element){
    elementsMap.remove(element);
  }

  public boolean containsKey(KeplerElements element){
    return elementsMap.containsKey(element);
  }
}
