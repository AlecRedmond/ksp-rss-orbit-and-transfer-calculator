package org.example.equations.method;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;

@Data
@Getter
@Setter
@NoArgsConstructor
public class KeplerianMethod {
  private Keplerian keplerian;

  public KeplerianMethod(Body body, double apsis1, double apsis2, boolean isFromSeaLevel) {
    double apoapsis = apsis1;
    double periapsis = apsis2;

    if (apsis1 < apsis2) {
      periapsis = apsis1;
      apoapsis = apsis2;
    }

    this.keplerian = new Keplerian();
    this.keplerian.setBody(body);

    if (isFromSeaLevel) {
      apoapsis += body.getRadius();
      periapsis += body.getRadius();
    }

    this.keplerian.setApoapsis(apoapsis);
    this.keplerian.setPeriapsis(periapsis);
    setEllipseElements(apoapsis, periapsis);
  }

  private void setEllipseElements(double trueApoapsis, double truePeriapsis) {
    double semiMajorAxis = (trueApoapsis + truePeriapsis) / 2;
    double eccentricity = (trueApoapsis - truePeriapsis) / (trueApoapsis + truePeriapsis);
    this.keplerian.setSemiMajorAxis(semiMajorAxis);
    this.keplerian.setEccentricity(eccentricity);
  }
}
