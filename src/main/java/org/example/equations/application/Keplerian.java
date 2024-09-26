package org.example.equations.application;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.equations.application.keplerianelements.Apoapsis;
import org.example.equations.application.keplerianelements.Eccentricity;
import org.example.equations.application.keplerianelements.Periapsis;
import org.example.equations.application.keplerianelements.SemiMajorAxis;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Keplerian {
  private Apoapsis apoapsis = new Apoapsis();
  private Periapsis periapsis = new Periapsis();
  private Eccentricity eccentricity = new Eccentricity();
  private SemiMajorAxis semiMajorAxis = new SemiMajorAxis();
  private double inclination, longitudeOfAscendingNode;
  private double argumentOfPeriapsis, trueAnomaly;
  private Body body = Body.EARTH;

  public void setAllToZero() {
    this.eccentricity.set(0.0);
    this.semiMajorAxis.set(0.0);
    this.apoapsis.set(0.0);
    this.periapsis.set(0.0);
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

  public void setHold(boolean holdValue, Class aClass) {
    if (aClass.equals(Eccentricity.class)) {
      this.eccentricity.setHold(holdValue);
    } else if (aClass.equals(SemiMajorAxis.class)) {
      this.semiMajorAxis.setHold(holdValue);
    } else if (aClass.equals(Apoapsis.class)) {
      this.apoapsis.setHold(holdValue);
    } else if (aClass.equals(Periapsis.class)) {
      this.periapsis.setHold(holdValue);
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
    }
    return false;
  }
}
