package org.artools.orbitcalculator.equations.application;

import static org.artools.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;
import org.artools.equations.application.keplerianelements.*;
import org.artools.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
public class Orbit {
  private Body body;

  private Map<KeplerEnums, Kepler> keplarianElements = new LinkedHashMap<>();

  public Orbit(Body body) {
    this.body = body;
    buildKeplarianElements();
  }

  private void buildKeplarianElements() {
    keplarianElements.put(APOAPSIS, new Apoapsis(0.0));
    keplarianElements.put(PERIAPSIS, new Periapsis(0.0));
    keplarianElements.put(ECCENTRICITY, new Eccentricity(0.0));
    keplarianElements.put(SEMI_MAJOR_AXIS, new SemiMajorAxis(0.0));
    keplarianElements.put(ORBITAL_PERIOD, new OrbitalPeriod(0.0));
    keplarianElements.put(VELOCITY_APOAPSIS, new VelocityApoapsis(0.0));
    keplarianElements.put(VELOCITY_PERIAPSIS, new VelocityPeriapsis(0.0));
    keplarianElements.put(INCLINATION, new Inclination(0.0));
    keplarianElements.put(RIGHT_ASCENSION, new RightAscension(0.0));
    keplarianElements.put(ARGUMENT_PE,new ArgumentPE(0.0));
    keplarianElements.put(NODAL_PRECESSION, new NodalPrecession(0.0));
  }

  public Orbit() {
    this.body = Body.EARTH;
    buildKeplarianElements();
  }

  public void setFromString(KeplerEnums keplerEnums, String inputString) {
    keplarianElements.get(keplerEnums).setFromString(inputString);
  }

  public String getAsString(KeplerEnums keplerEnums) {
    return keplarianElements.get(keplerEnums).getAsString();
  }

  public double getDataFor(KeplerEnums keplerEnums) {
    return keplarianElements.get(keplerEnums).getData();
  }

  public void setDataFor(KeplerEnums keplerEnums, double data) {
    keplarianElements.get(keplerEnums).setData(data);
  }

  public String getDisplayName(KeplerEnums keplerEnums) {
    return keplarianElements.get(keplerEnums).displayName();
  }

  public void printAll(){
    keplarianElements.keySet().forEach(key -> {
      System.out.println(getDisplayName(key) + " : " + getAsString(key));
    });
  }

}
