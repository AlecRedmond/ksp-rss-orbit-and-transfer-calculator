package org.example.equations.method;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.equations.application.Keplerian;
import org.example.equations.application.keplerianelements.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class KeplerianMethod {
  private Keplerian keplerian = new Keplerian();
  private HashMap<Class, String> dataToParse = new HashMap<>();

  public void calculateMissing() {
    setAllInputData();

    boolean apoapsis = heldValue(Apoapsis.class);
    boolean periapsis = heldValue(Periapsis.class);
    boolean eccentricity = heldValue(Eccentricity.class);
    boolean semiMajorAxis = heldValue(SemiMajorAxis.class);
    boolean orbitalPeriod = heldValue(OrbitalPeriod.class);
    boolean velocityAP = heldValue(VelocityApoapsis.class);
    boolean velocityPE = heldValue(VelocityPeriapsis.class);

    if (orbitalPeriod) {
      this.keplerian = FillEquations.convertOrbitalPeriodToSMA(this.keplerian);
      semiMajorAxis = true;
    }

    if (velocityAP || velocityPE) {
      if (apoapsis || periapsis) {
        this.keplerian =
            FillEquations.calculateSMAFromVelocityAndAltitude(this.keplerian, periapsis);
        semiMajorAxis = true;
      }
      if (semiMajorAxis) {
        this.keplerian =
            FillEquations.calculateAltitudeFromVelocityAndSMA(this.keplerian, velocityPE);
        if (velocityPE) {
          periapsis = true;
        } else {
          apoapsis = true;
        }
      }
    }

    if (apoapsis && periapsis) {
      this.keplerian = FillEquations.findPeriapsisApoapsis(this.keplerian);
    }

    if (periapsis && eccentricity || apoapsis && eccentricity) {
      this.keplerian = FillEquations.findApsisEccentricity(this.keplerian, periapsis);
    }

    if (periapsis && semiMajorAxis || apoapsis && semiMajorAxis) {
      this.keplerian = FillEquations.findApsisSemiMajorAxis(this.keplerian, periapsis);
    }

    if (semiMajorAxis && eccentricity) {
      this.keplerian = FillEquations.findEccentricitySemiMajorAxis(this.keplerian);
    }
  }

  private void setAllInputData() {
    for (Map.Entry<Class, String> entry : this.dataToParse.entrySet()) {
      setFromString(entry.getValue(), entry.getKey());
    }
  }

  private boolean heldValue(Class aClass) {
    return dataToParse.containsKey(aClass);
  }

  public void setFromString(String string, Class aClass) {
    this.keplerian.setFromString(string, aClass);
  }

  public String getAsString(Class aClass) {
    return this.keplerian.getAsString(aClass);
  }
}
