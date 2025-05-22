package org.example.equations.application;

import lombok.Getter;

@Getter
public enum Body {
  SUN(695.7e6, 1.32712440018e20, 0),
  EARTH(6.3781e+6, 3.9860044188e+14, 1.08262668e-3);

  private final double radius;
  private final double mu;
  private final double j2;

  Body(double radius, double mu, double j2) {
    this.radius = radius;
    this.mu = mu;
    this.j2 = j2;
  }
}
