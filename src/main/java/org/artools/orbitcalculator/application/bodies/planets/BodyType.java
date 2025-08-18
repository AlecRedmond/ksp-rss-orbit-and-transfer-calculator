package org.artools.orbitcalculator.application.bodies.planets;

import lombok.Getter;

@Getter
public enum BodyType {
  SUN,
  // GAS GIANTS
  JUPITER,
  SATURN,
  NEPTUNE,
  URANUS,
  // TERRESTRIAL PLANETS
  VENUS,
  MARS,
  MERCURY,
  // EARTH AND MOON
  EARTH,
  MOON
}
