package org.example.equations.application;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
  private Apoapsis apoapsis = new Apoapsis(0.0);
  private Periapsis periapsis = new Periapsis(0.0);
  private Eccentricity eccentricity = new Eccentricity(0.0);
  private SemiMajorAxis semiMajorAxis = new SemiMajorAxis(0.0);
  private VelocityPeriapsis velocityPeriapsis = new VelocityPeriapsis(0.0);
  private VelocityApoapsis velocityApoapsis = new VelocityApoapsis(0.0);
  private OrbitalPeriod orbitalPeriod = new OrbitalPeriod(0.0);
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
    this.orbitalPeriod.set(0.0);
    this.inclination = 0;
    this.longitudeOfAscendingNode = 0;
    this.argumentOfPeriapsis = 0;
    this.trueAnomaly = 0;
  }

  public LinkedList<Class> keplerianClassList() {
    return new LinkedList<>(
        List.of(
            this.apoapsis.getClass(),
            this.periapsis.getClass(),
            this.eccentricity.getClass(),
            this.semiMajorAxis.getClass(),
            this.orbitalPeriod.getClass(),
            this.velocityApoapsis.getClass(),
            this.velocityPeriapsis.getClass()));
  }

  public void setFromString(String string, KeplerInterface<?> keplerInterface) {
    keplerInterface.setFromString(string);
    Field[] fields = this.getClass().getDeclaredFields();

    for (Field field : fields) {
      if (field.getName().equalsIgnoreCase(keplerInterface.getClass().getSimpleName())) {
        try {
          field.set(this, keplerInterface);
        } catch (IllegalAccessException ignored) {
        }
      }
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
    } else if (aClass.equals(OrbitalPeriod.class)) {
      return this.orbitalPeriod.getAsString();
    }
    return "";
  }
}
