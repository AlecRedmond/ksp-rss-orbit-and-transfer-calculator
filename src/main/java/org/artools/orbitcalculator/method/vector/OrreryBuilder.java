package org.artools.orbitcalculator.method.vector;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.artools.orbitcalculator.application.bodies.planets.BodyName;
import org.artools.orbitcalculator.application.bodies.planets.*;
import org.artools.orbitcalculator.application.vector.Orrery;

@Getter
public class OrreryBuilder {
  private final Orrery orrery;

  public OrreryBuilder() {
    List<Planet> planets = initializeAllPlanets();
    orrery = new Orrery(planets);
    Planet sun = orrery.getPlanetByName(BodyName.SUN);
    OrreryUtils utils = new OrreryUtils(orrery);
    utils.centreBody(sun);
  }

  private List<Planet> initializeAllPlanets() {
    List<Planet> planets = new ArrayList<>();
    for (BodyName type : BodyName.values()) {
      switch (type) {
        case SUN -> planets.add(new Sun());
        case MERCURY -> planets.add(new Mercury());
        case VENUS -> planets.add(new Venus());
        case EARTH -> planets.add(new Earth());
        case MOON -> planets.add(new Moon());
        case MARS -> planets.add(new Mars());
        case JUPITER -> planets.add(new Jupiter());
        case SATURN -> planets.add(new Saturn());
        case URANUS -> planets.add(new Uranus());
        case NEPTUNE -> planets.add(new Neptune());
        default ->
            throw new IllegalStateException(
                "Unexpected value during Orrery Initialization: " + type);
      }
    }
    return planets;
  }
}
