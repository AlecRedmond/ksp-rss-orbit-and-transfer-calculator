package org.example.equations.application;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.equations.application.keplerianelements.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Keplerian {
  private Apoapsis apoapsis = new Apoapsis();
  private Periapsis periapsis = new Periapsis();
  private Eccentricity eccentricity = new Eccentricity();
  private SemiMajorAxis semiMajorAxis = new SemiMajorAxis();
  private VelocityPeriapsis velocityPeriapsis;
  private VelocityApoapsis velocityApoapsis;
  private double inclination, longitudeOfAscendingNode;
  private double argumentOfPeriapsis, trueAnomaly;
  private Body body = Body.EARTH;

  public void setAllToZero() {
    this.eccentricity.set(0.0);
    this.semiMajorAxis.set(0.0);
    this.apoapsis.set(0.0);
    this.periapsis.set(0.0);
    this.velocityPeriapsis.set(0.0);
    this.velocityApoapsis.set(0.0);
    this.inclination = 0;
    this.longitudeOfAscendingNode = 0;
    this.argumentOfPeriapsis = 0;
    this.trueAnomaly = 0;
  }

  public double getApoapsis() {
    return this.apoapsis.get();
  }

  public double getPeriapsis() {
    return this.periapsis.get();
  }

  public double getEccentricity() {
    return this.eccentricity.get();
  }

  public double getSemiMajorAxis() {
    return this.semiMajorAxis.get();
  }

  public double getVelocityApoapsis() {
    return this.velocityApoapsis.get();
  }

  public double getVelocityPeriapsis() {
    return this.velocityPeriapsis.get();
  }

  public void setApoapsis(double data) {
    this.apoapsis.set(data);
  }

  public void setPeriapsis(double data) {
    this.periapsis.set(data);
  }

  public void setEccentricity(double data) {
    this.eccentricity.set(data);
  }

  public void setSemiMajorAxis(double data) {
    this.semiMajorAxis.set(data);
  }

  public void setVelocityApoapsis(double data) {
    this.velocityApoapsis.set(data);
  }

  public void setVelocityPeriapsis(double data) {
    this.velocityPeriapsis.set(data);
  }

  public void setHold(boolean holdValue, Class aClass) {
    if (aClass.equals(Eccentricity.class)) {
      this.eccentricity.setHold(holdValue);
    } else if (aClass.equals(SemiMajorAxis.class)) {
      this.semiMajorAxis.setHold(holdValue);
    } else if (aClass.equals(Apoapsis.class)) {
      this.apoapsis.setHold(holdValue);
    } else if (aClass.equals(Periapsis.class)) {
      this.periapsis.setHold(holdValue);
    } else if (aClass.equals(VelocityPeriapsis.class)) {
      this.velocityPeriapsis.setHold(holdValue);
    } else if (aClass.equals(VelocityApoapsis.class)) {
      this.velocityApoapsis.setHold(holdValue);
    }
  }

  public boolean isHeld(Class aClass) {
    if (aClass.equals(Eccentricity.class)) {
      return this.eccentricity.isHeld();
    } else if (aClass.equals(SemiMajorAxis.class)) {
      return this.semiMajorAxis.isHeld();
    } else if (aClass.equals(Apoapsis.class)) {
      return this.apoapsis.isHeld();
    } else if (aClass.equals(Periapsis.class)) {
      return this.periapsis.isHeld();
    } else if (aClass.equals(VelocityApoapsis.class)) {
      return this.velocityApoapsis.isHeld();
    } else if (aClass.equals(VelocityPeriapsis.class)) {
      return this.velocityPeriapsis.isHeld();
    }
    return false;
  }

  public void setFromString(String string, Class aClass) {
    if (aClass.equals(Eccentricity.class)) {
      this.eccentricity.setFromString(string);
    } else if (aClass.equals(SemiMajorAxis.class)) {
      this.semiMajorAxis.setFromString(string);
    } else if (aClass.equals(Apoapsis.class)) {
      this.apoapsis.setFromString(string);
    } else if (aClass.equals(Periapsis.class)) {
      this.periapsis.setFromString(string);
    } else if (aClass.equals(VelocityPeriapsis.class)) {
      this.velocityPeriapsis.setFromString(string);
    } else if (aClass.equals(VelocityApoapsis.class)) {
      this.velocityApoapsis.setFromString(string);
    }
  }

  public String getAsString(Class aClass) {
    if (aClass.equals(Eccentricity.class)) {
      return this.eccentricity.getAsString();
    } else if (aClass.equals(SemiMajorAxis.class)) {
      return this.semiMajorAxis.getAsString();
    } else if (aClass.equals(Apoapsis.class)) {
      return this.apoapsis.getAsString();
    } else if (aClass.equals(Periapsis.class)) {
      return this.periapsis.getAsString();
    } else if (aClass.equals(VelocityPeriapsis.class)) {
      return this.velocityPeriapsis.getAsString();
    } else if (aClass.equals(VelocityApoapsis.class)) {
      return this.velocityApoapsis.getAsString();
    }
    return "";
  }
}
