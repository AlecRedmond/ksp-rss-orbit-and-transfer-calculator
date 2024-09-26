package org.example.equations.method;

import java.util.List;
import java.util.stream.Stream;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.equations.application.Keplerian;
import org.example.equations.application.keplerianelements.Apoapsis;
import org.example.equations.application.keplerianelements.Eccentricity;
import org.example.equations.application.keplerianelements.Periapsis;
import org.example.equations.application.keplerianelements.SemiMajorAxis;

@Data
@Getter
@Setter
@NoArgsConstructor
public class KeplerianMethod {
  private Keplerian keplerian = new Keplerian();

  public void calculateMissing() {
    boolean periapsis = this.keplerian.isHeld(Periapsis.class);
    boolean apoapsis = this.keplerian.isHeld(Apoapsis.class);
    boolean semiMajorAxis = this.keplerian.isHeld(SemiMajorAxis.class);
    boolean eccentricity = this.keplerian.isHeld(Eccentricity.class);

    List<Boolean> streamOfBools =
        Stream.of(periapsis, apoapsis, semiMajorAxis, eccentricity)
            .filter(element -> element)
            .toList();

    if (streamOfBools.size() != 2) {
      this.keplerian.setAllToZero();
      return;
    }

    if (periapsis && apoapsis) {
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

  public void setFromString(String string, Class aClass) {
    this.keplerian.setFromString(string, aClass);
  }

  public void setHold(boolean holdValue, Class aClass) {
    this.keplerian.setHold(holdValue, aClass);
  }

  public String getAsString(Class aClass) {
    return this.keplerian.getAsString(aClass);
  }
}
