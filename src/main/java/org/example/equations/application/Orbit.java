package org.example.equations.application;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.equations.application.keplerianelements.*;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
@Getter
@Setter
public class Orbit {
  private Body body;

  private Map<KeplerEnums, Kepler> keplarianElements = new HashMap<>();

  public Orbit(Body body) {
    this.body = body;
    buildKeplarianElements();
  }

  public Orbit() {
    this.body = Body.EARTH;
    buildKeplarianElements();
  }

  private void buildKeplarianElements() {
    keplarianElements.putAll(
        Map.of(
            APOAPSIS, new Apoapsis(0.0),
            PERIAPSIS, new Periapsis(0.0),
            ECCENTRICITY, new Eccentricity(0.0),
            SEMI_MAJOR_AXIS, new SemiMajorAxis(0.0),
            ORBITAL_PERIOD, new OrbitalPeriod(0.0),
            VELOCITY_APOAPSIS, new VelocityApoapsis(0.0),
            VELOCITY_PERIAPSIS, new VelocityPeriapsis(0.0)));
  }

  public void setFromString(String inputData, Kepler kepler) {
    kepler.setFromString(inputData);
    keplarianElements.replace(kepler.getType(), kepler);
  }

  public String getAsString(Kepler kepler) {
    return keplarianElements.get(kepler.getType()).getAsString();
  }

  public double getDataFor(KeplerEnums keplerEnums) {
    return keplarianElements.get(keplerEnums).getData();
  }

  public void setDataFor(KeplerEnums keplerEnums, double data) {
    keplarianElements.get(keplerEnums).setData(data);
  }

  public String getDisplayName(KeplerEnums keplerEnums){ return keplarianElements.get(keplerEnums).displayName();}
}
