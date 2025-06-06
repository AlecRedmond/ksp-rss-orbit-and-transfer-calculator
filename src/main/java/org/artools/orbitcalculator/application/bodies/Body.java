package org.artools.orbitcalculator.application.bodies;

import org.artools.orbitcalculator.application.bodies.astralbodies.*;
import org.artools.orbitcalculator.application.vector.MotionState;


public enum Body {
  SUN(new Sun()),
  JUPITER(new Jupiter()),
  SATURN(new Saturn()),
  NEPTUNE(new Neptune()),
  URANUS(new Uranus()),
  VENUS(new Venus()),
  MARS(new Mars()),
  MERCURY(new Mercury()),
  MOON(new Moon()),
  EARTH(new Earth()),
  CRAFT(new Craft());

  private final AstralBody astralBody;

  Body(AstralBody astralBody) {
    this.astralBody = astralBody;
  }

  public double getJ2() {
    return astralBody.getJ2();
  }

  public double getRadius() {
    return astralBody.getBodyRadius();
  }

  public double getMass() {
    return astralBody.getMass();
  }

  public double getMu() {
    return astralBody.getMu();
  }

  public MotionState get1951Jan1State(){
    return astralBody.getMotionState1951Jan1();
  }
}
