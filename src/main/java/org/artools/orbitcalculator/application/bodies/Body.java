package org.artools.orbitcalculator.application.bodies;

import lombok.Getter;

@Getter
public enum Body {
  SUN(695700, 1.327124400189E20, 0),
  JUPITER(69911,1.266865349E17,0),
  SATURN(58232,3.79311879E17,0),
  NEPTUNE(24622,6.8365299E15,0),
  URANUS(25362,5.7939399E15,0),
  VENUS(6051.8,3.248585926E14,0),
  MARS(3389.5,4.2828372E13,0),
  MERCURY(2439.7,2.203209E13,0),
  MOON(1737.4,4.90486959E12,0),
  EARTH(6.3781e+3, 3.9860044188E14, 1.08262668e-3),
  CRAFT(0,0,0);

  private final double radius;
  private final double mass;
  private final double mu;
  private final double j2;

  Body(double radiusKm, double mu, double j2) {
    this.radius = radiusKm * 1E03;
    this.mu = mu;
    this.mass = mu / 6.6743015E-11;
    this.j2 = j2;
  }
}
