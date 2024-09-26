package org.example.equations.method;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;
import org.example.equations.application.keplerianelements.Apoapsis;
import org.example.equations.application.keplerianelements.Eccentricity;
import org.example.equations.application.keplerianelements.Periapsis;
import org.example.equations.application.keplerianelements.SemiMajorAxis;
import org.example.gui.method.VVDataElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Data
@Getter
@Setter
@NoArgsConstructor
public class KeplerianMethod {
  private Keplerian keplerian = new Keplerian();
  private ArrayList<VVDataElement> listOfHeldParameters = new ArrayList<>();

  public void setFromDataElement(VVDataElement vvDataElement) {
    String elementName = vvDataElement.getParameterName().toLowerCase();
    boolean holdEnabled = vvDataElement.isHeld();
    double data = vvDataElement.getData();
    if (holdEnabled) {
      this.listOfHeldParameters.add(vvDataElement);
      setFromString(elementName, data);
    }
  }

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

  private void setFromString(String string, double data) {
    switch (string) {
      case "periapsis" -> this.keplerian.setPeriapsis(data);
      case "apoapsis" -> this.keplerian.setApoapsis(data);
      case "eccentricity" -> this.keplerian.setEccentricity(data);
      case "semi-major axis" -> this.keplerian.setSemiMajorAxis(data);
    }
  }

  public double getFromParameterName(String parameter) {
    switch (parameter) {
      case "periapsis" -> {
        return this.keplerian.getPeriapsis();
      }
      case "apoapsis" -> {
        return this.keplerian.getApoapsis();
      }
      case "eccentricity" -> {
        return this.keplerian.getEccentricity();
      }
      case "semi-major axis" -> {
        return this.keplerian.getSemiMajorAxis();
      }
    }
    return 0;
  }
}
