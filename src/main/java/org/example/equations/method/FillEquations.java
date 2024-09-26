package org.example.equations.method;

import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;

public class FillEquations {
  public static Keplerian findPeriapsisApoapsis(Keplerian keplerian) {
    double apoapsis = keplerian.getApoapsis();
    double periapsis = keplerian.getPeriapsis();
    Body body = keplerian.getBody();

    if (apoapsis < periapsis) {
      keplerian.setPeriapsis(apoapsis);
      keplerian.setApoapsis(periapsis);
      apoapsis = keplerian.getApoapsis();
      periapsis = keplerian.getPeriapsis();
    }

    apoapsis += body.getRadius();
    periapsis += body.getRadius();

    keplerian.setSemiMajorAxis((apoapsis + periapsis) / 2);
    keplerian.setEccentricity((apoapsis - periapsis) / (apoapsis + periapsis));
    return keplerian;
  }

  public static Keplerian findApsisEccentricity(Keplerian keplerian, boolean hasPeriapsis) {
    double apoapsis;
    double periapsis;
    double eccentricity = keplerian.getEccentricity();
    double semiMajorAxis;
    if (hasPeriapsis) {
      periapsis = keplerian.getPeriapsis() + keplerian.getBody().getRadius();
      semiMajorAxis = periapsis / (1 - eccentricity);
      keplerian.setSemiMajorAxis(semiMajorAxis);
    } else {
      apoapsis = keplerian.getApoapsis() + keplerian.getBody().getRadius();
      semiMajorAxis = apoapsis / (1 + eccentricity);
      keplerian.setSemiMajorAxis(semiMajorAxis);
    }
    return findEccentricitySemiMajorAxis(keplerian);
  }

  public static Keplerian findApsisSemiMajorAxis(Keplerian keplerian, boolean hasPeriapsis) {
    double apoapsis;
    double periapsis;
    double semiMajorAxis = keplerian.getSemiMajorAxis();
    if (hasPeriapsis) {
      periapsis = keplerian.getPeriapsis() + keplerian.getBody().getRadius();
      keplerian.setEccentricity(1 - (periapsis / semiMajorAxis));
    } else {
      apoapsis = keplerian.getApoapsis() + keplerian.getBody().getRadius();
      keplerian.setEccentricity((apoapsis / semiMajorAxis) - 1);
    }
    return findEccentricitySemiMajorAxis(keplerian);
  }

  public static Keplerian findEccentricitySemiMajorAxis(Keplerian keplerian) {
    double eccentricity = keplerian.getEccentricity();
    double semiMajorAxis = keplerian.getSemiMajorAxis();
    keplerian.setApoapsis((semiMajorAxis * (1 + eccentricity)) - keplerian.getBody().getRadius());
    keplerian.setPeriapsis((semiMajorAxis * (1 - eccentricity)) - keplerian.getBody().getRadius());
    return keplerian;
  }
}
