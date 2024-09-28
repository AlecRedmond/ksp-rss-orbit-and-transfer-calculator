package org.example.equations.method;

import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;

public class FillEquations {
  public static Keplerian findPeriapsisApoapsis(Keplerian keplerian) {
    double apoapsis = keplerian.getApoapsis().get();
    double periapsis = keplerian.getPeriapsis().get();
    Body body = keplerian.getBody();

    if (apoapsis < periapsis) {
      keplerian.getPeriapsis().set(apoapsis);
      keplerian.getApoapsis().set(periapsis);
      apoapsis = keplerian.getApoapsis().get();
      periapsis = keplerian.getPeriapsis().get();
    }

    apoapsis += body.getRadius();
    periapsis += body.getRadius();

    keplerian.getSemiMajorAxis().set((apoapsis + periapsis) / 2);
    keplerian.getEccentricity().set((apoapsis - periapsis) / (apoapsis + periapsis));
    keplerian = convertSemiMajorAxisToOrbitalPeriod(keplerian);
    keplerian = calculateBothVelocities(keplerian);
    return keplerian;
  }

  public static Keplerian findApsisEccentricity(Keplerian keplerian, boolean hasPeriapsis) {
    double apoapsis;
    double periapsis;
    double eccentricity = keplerian.getEccentricity().get();
    double semiMajorAxis;
    if (hasPeriapsis) {
      periapsis = keplerian.getPeriapsis().get() + keplerian.getBody().getRadius();
      semiMajorAxis = periapsis / (1 - eccentricity);
      keplerian.getSemiMajorAxis().set(semiMajorAxis);
    } else {
      apoapsis = keplerian.getApoapsis().get() + keplerian.getBody().getRadius();
      semiMajorAxis = apoapsis / (1 + eccentricity);
      keplerian.getSemiMajorAxis().set(semiMajorAxis);
    }
    return findEccentricitySemiMajorAxis(keplerian);
  }

  public static Keplerian findApsisSemiMajorAxis(Keplerian keplerian, boolean hasPeriapsis) {
    double apoapsis;
    double periapsis;
    double semiMajorAxis = keplerian.getSemiMajorAxis().get();
    if (hasPeriapsis) {
      periapsis = keplerian.getPeriapsis().get() + keplerian.getBody().getRadius();
      keplerian.getEccentricity().set(1 - (periapsis / semiMajorAxis));
    } else {
      apoapsis = keplerian.getApoapsis().get() + keplerian.getBody().getRadius();
      keplerian.getEccentricity().set((apoapsis / semiMajorAxis) - 1);
    }
    return findEccentricitySemiMajorAxis(keplerian);
  }

  public static Keplerian findEccentricitySemiMajorAxis(Keplerian keplerian) {
    double eccentricity = keplerian.getEccentricity().get();
    double semiMajorAxis = keplerian.getSemiMajorAxis().get();
    keplerian
        .getApoapsis()
        .set((semiMajorAxis * (1 + eccentricity)) - keplerian.getBody().getRadius());
    keplerian
        .getPeriapsis()
        .set((semiMajorAxis * (1 - eccentricity)) - keplerian.getBody().getRadius());
    keplerian = convertSemiMajorAxisToOrbitalPeriod(keplerian);
    keplerian = calculateBothVelocities(keplerian);
    return keplerian;
  }

  public static Keplerian convertOrbitalPeriodToSMA(Keplerian keplerian) {
    double orbitalPeriod = keplerian.getOrbitalPeriod().get();
    double mu = keplerian.getBody().getMu();
    double semiMajorAxis =
        Math.pow((orbitalPeriod * Math.sqrt(mu)) / (2 * Math.PI), ((double) 2 / 3));
    keplerian.getSemiMajorAxis().set(semiMajorAxis);
    return keplerian;
  }

  public static Keplerian convertSemiMajorAxisToOrbitalPeriod(Keplerian keplerian) {
    double semiMajorAxis = keplerian.getSemiMajorAxis().get();
    double mu = keplerian.getBody().getMu();
    double orbitalPeriod = (2 * Math.PI) * Math.sqrt((Math.pow(semiMajorAxis, 3)) / mu);
    keplerian.getOrbitalPeriod().set(orbitalPeriod);
    return keplerian;
  }

  public static Keplerian calculateSMAFromVelocityAndAltitude(
      Keplerian keplerian, boolean periapsis) {

    if (periapsis) {
      keplerian
          .getSemiMajorAxis()
          .set(
              HohmannTransfer.smaFromVelocityAndAltitude(
                  keplerian.getVelocityPeriapsis().get(), keplerian.getPeriapsis().get()));

      return keplerian;
    } else {
      keplerian
          .getSemiMajorAxis()
          .set(
              HohmannTransfer.smaFromVelocityAndAltitude(
                  keplerian.getVelocityApoapsis().get(), keplerian.getApoapsis().get()));

      return keplerian;
    }
  }

  public static Keplerian calculateAltitudeFromVelocityAndSMA(
      Keplerian keplerian, boolean periapsis) {

    if (periapsis) {
      keplerian
          .getPeriapsis()
          .set(
              HohmannTransfer.altitudeFromVelocityAndSMA(
                  keplerian.getVelocityPeriapsis().get(), keplerian.getSemiMajorAxis().get()));

      return keplerian;
    } else {
      keplerian
          .getApoapsis()
          .set(
              HohmannTransfer.altitudeFromVelocityAndSMA(
                  keplerian.getVelocityApoapsis().get(), keplerian.getSemiMajorAxis().get()));

      return keplerian;
    }
  }

  public static Keplerian calculateBothVelocities(Keplerian keplerian) {
    double altitudePE = keplerian.getPeriapsis().get();
    double altitudeAP = keplerian.getApoapsis().get();
    double sma = keplerian.getSemiMajorAxis().get();
    double velocityAP = HohmannTransfer.velocityFromAltitudeAndSMA(altitudeAP, sma);
    double velocityPE = HohmannTransfer.velocityFromAltitudeAndSMA(altitudePE, sma);
    keplerian.getVelocityApoapsis().set(velocityAP);
    keplerian.getVelocityPeriapsis().set(velocityPE);
    return keplerian;
  }
}
