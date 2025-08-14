package org.artools.orbitcalculator.application.bodies;

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
  MOON,
  // CRAFT
  CRAFT
}
