package org.example.equations.application.keplerianelements;

public interface Kepler {

  enum KeplarianElement {
    APOAPSIS,
    ECCENTRICITY,
    ORBITAL_PERIOD,
    PERIAPSIS,
    SEMI_MAJOR_AXIS,
    VELOCITY,
    VELOCITY_APOAPSIS,
    VELOCITY_PERIAPSIS
  }

  KeplarianElement getType();

  String getAsString();

  void setFromString(String string);

  String displayName();

  String unitSI();

  double getData();

  void setData(double data);
}
