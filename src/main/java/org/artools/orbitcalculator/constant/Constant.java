package org.artools.orbitcalculator.constant;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Constant {
  public static final double GRAVITATIONAL_CONSTANT = 6.6743015E-11; // m^3 kg^-1 s^-2
  public static final double EARTH_STANDARD_GRAVITY = 9.80665; // m s^-2
  public static final Duration DEFAULT_CRAFT_LIFETIME = Duration.of(100, ChronoUnit.YEARS);

  private Constant() {}
}
