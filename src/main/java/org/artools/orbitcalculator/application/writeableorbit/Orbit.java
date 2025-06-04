package org.artools.orbitcalculator.application.writeableorbit;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import org.artools.orbitcalculator.application.bodies.Body;
import org.artools.orbitcalculator.application.writeableorbit.keplerianelements.*;

@Data
public class Orbit {
  private Body body;

  private Map<Kepler.KeplerEnums, Kepler> keplarianElements = new LinkedHashMap<>();

  public Orbit(Body body) {
    this.body = body;
    buildKeplarianElements();
  }

  private void buildKeplarianElements() {
    keplarianElements.put(Kepler.KeplerEnums.APOAPSIS, new Apoapsis(0.0));
    keplarianElements.put(Kepler.KeplerEnums.PERIAPSIS, new Periapsis(0.0));
    keplarianElements.put(Kepler.KeplerEnums.ECCENTRICITY, new Eccentricity(0.0));
    keplarianElements.put(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, new SemiMajorAxis(0.0));
    keplarianElements.put(Kepler.KeplerEnums.ORBITAL_PERIOD, new OrbitalPeriod(0.0));
    keplarianElements.put(Kepler.KeplerEnums.VELOCITY_APOAPSIS, new VelocityApoapsis(0.0));
    keplarianElements.put(Kepler.KeplerEnums.VELOCITY_PERIAPSIS, new VelocityPeriapsis(0.0));
    keplarianElements.put(Kepler.KeplerEnums.INCLINATION, new Inclination(0.0));
    keplarianElements.put(Kepler.KeplerEnums.RIGHT_ASCENSION, new RightAscension(0.0));
    keplarianElements.put(Kepler.KeplerEnums.ARGUMENT_PE, new ArgumentPE(0.0));
    keplarianElements.put(Kepler.KeplerEnums.NODAL_PRECESSION, new NodalPrecession(0.0));
  }

  public Orbit() {
    this.body = Body.EARTH;
    buildKeplarianElements();
  }

  public void setFromString(Kepler.KeplerEnums keplerEnums, String inputString) {
    keplarianElements.get(keplerEnums).setFromString(inputString);
  }

  public double getDataFor(Kepler.KeplerEnums keplerEnums) {
    return keplarianElements.get(keplerEnums).getData();
  }

  public void setDataFor(Kepler.KeplerEnums keplerEnums, double data) {
    keplarianElements.get(keplerEnums).setData(data);
  }

  public void printAll() {
    keplarianElements
        .keySet()
        .forEach(
            key -> {
              System.out.println(getDisplayName(key) + " : " + getAsString(key));
            });
  }

  public String getDisplayName(Kepler.KeplerEnums keplerEnums) {
    return keplarianElements.get(keplerEnums).displayName();
  }

  public String getAsString(Kepler.KeplerEnums keplerEnums) {
    return keplarianElements.get(keplerEnums).getAsString();
  }
}
