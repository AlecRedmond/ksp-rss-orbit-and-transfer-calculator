package org.example.equations.application.keplerianelements;

public interface Kepler {

  enum KeplerEnums {
    APOAPSIS,
    ECCENTRICITY,
    ORBITAL_PERIOD,
    PERIAPSIS,
    SEMI_MAJOR_AXIS,
    VELOCITY,
    VELOCITY_APOAPSIS,
    VELOCITY_PERIAPSIS,
    INCLINATION,
    RIGHT_ASCENSION, ARGUMENT_PE, NODAL_PRECESSION
  }

  KeplerEnums getType();

  String getAsString();

  void setFromString(String string);

  String displayName();

  String unitSI();

  double getData();

  void setData(double data);

}
