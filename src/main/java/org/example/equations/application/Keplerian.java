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
  private Body body = Body.EARTH;

  public void setAllToZero() {
    this.eccentricity.set(0.0);
    this.semiMajorAxis.set(0.0);
    this.apoapsis.set(0.0);
    this.periapsis.set(0.0);
    this.velocityPeriapsis.set(0.0);
    this.velocityApoapsis.set(0.0);
    this.orbitalPeriod.set(0.0);
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

  public String getAsString(KeplerInterface<?> keplerInterface) {
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      if(field.getName().equalsIgnoreCase(keplerInterface.getClass().getSimpleName())){
          try {
          } catch (IllegalAccessException e) {
              throw new RuntimeException(e);
          }
      }
    }
  }
}
