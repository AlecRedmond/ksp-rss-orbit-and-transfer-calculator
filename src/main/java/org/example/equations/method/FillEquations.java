package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplarianElement.*;

import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;

public class FillEquations {
  private static Body body = Body.EARTH;

  public static Keplerian findPeriapsisApoapsis(Keplerian keplerian) {
    double apoapsis = keplerian.getDataFor(APOAPSIS);
    double periapsis = keplerian.getDataFor(PERIAPSIS);
    Body body = keplerian.getBody();

    if (apoapsis < periapsis) {
      keplerian.setDataFor(PERIAPSIS, apoapsis);
      keplerian.setDataFor(APOAPSIS, periapsis);
      apoapsis = keplerian.getDataFor(APOAPSIS);
      periapsis = keplerian.getDataFor(PERIAPSIS);
    }

    apoapsis += body.getRadius();
    periapsis += body.getRadius();

    keplerian.setDataFor(SEMI_MAJOR_AXIS, (apoapsis + periapsis) / 2);
    keplerian.setDataFor(ECCENTRICITY, (apoapsis - periapsis) / (apoapsis + periapsis));
    keplerian = convertSemiMajorAxisToOrbitalPeriod(keplerian);
    keplerian = calculateBothVelocities(keplerian);
    return keplerian;
  }

  public static Keplerian findApsisEccentricity(Keplerian keplerian, boolean hasPeriapsis) {
    double apoapsis;
    double periapsis;
    double eccentricity = keplerian.getDataFor(ECCENTRICITY);
    double semiMajorAxis;
    if (hasPeriapsis) {
      periapsis = keplerian.getDataFor(PERIAPSIS) + keplerian.getBody().getRadius();
      semiMajorAxis = periapsis / (1 - eccentricity);
      keplerian.setDataFor(SEMI_MAJOR_AXIS, semiMajorAxis);
    } else {
      apoapsis = keplerian.getDataFor(APOAPSIS) + keplerian.getBody().getRadius();
      semiMajorAxis = apoapsis / (1 + eccentricity);
      keplerian.setDataFor(SEMI_MAJOR_AXIS, semiMajorAxis);
    }
    return findEccentricitySemiMajorAxis(keplerian);
  }

  public static Keplerian findApsisSemiMajorAxis(Keplerian keplerian, boolean hasPeriapsis) {
    double apoapsis;
    double periapsis;
    double semiMajorAxis = keplerian.getDataFor(SEMI_MAJOR_AXIS);
    if (hasPeriapsis) {
      periapsis = keplerian.getDataFor(PERIAPSIS) + keplerian.getBody().getRadius();
      keplerian.setDataFor(ECCENTRICITY, 1 - (periapsis / semiMajorAxis));
    } else {
      apoapsis = keplerian.getDataFor(APOAPSIS) + keplerian.getBody().getRadius();
      keplerian.setDataFor(ECCENTRICITY, (apoapsis / semiMajorAxis) - 1);
    }
    return findEccentricitySemiMajorAxis(keplerian);
  }

  public static Keplerian findEccentricitySemiMajorAxis(Keplerian keplerian) {
    double eccentricity = keplerian.getDataFor(ECCENTRICITY);
    double semiMajorAxis = keplerian.getDataFor(SEMI_MAJOR_AXIS);
    keplerian.setDataFor(
        APOAPSIS, (semiMajorAxis * (1 + eccentricity)) - keplerian.getBody().getRadius());
    keplerian.setDataFor(
        PERIAPSIS, (semiMajorAxis * (1 - eccentricity)) - keplerian.getBody().getRadius());
    keplerian = convertSemiMajorAxisToOrbitalPeriod(keplerian);
    keplerian = calculateBothVelocities(keplerian);
    return keplerian;
  }

  public static Keplerian convertOrbitalPeriodToSMA(Keplerian keplerian) {
    double orbitalPeriod = keplerian.getDataFor(ORBITAL_PERIOD);
    double mu = keplerian.getBody().getMu();
    double semiMajorAxis =
        Math.pow((orbitalPeriod * Math.sqrt(mu)) / (2 * Math.PI), ((double) 2 / 3));
    keplerian.setDataFor(SEMI_MAJOR_AXIS, semiMajorAxis);
    return keplerian;
  }

  public static Keplerian convertSemiMajorAxisToOrbitalPeriod(Keplerian keplerian) {
    double semiMajorAxis = keplerian.getDataFor(SEMI_MAJOR_AXIS);
    double mu = keplerian.getBody().getMu();
    double orbitalPeriod = (2 * Math.PI) * Math.sqrt((Math.pow(semiMajorAxis, 3)) / mu);
    keplerian.setDataFor(ORBITAL_PERIOD, orbitalPeriod);
    return keplerian;
  }

  public static Keplerian calculateSMAFromVelocityAndAltitude(
      Keplerian keplerian, boolean periapsis) {

    if (periapsis) {
      keplerian.setDataFor(
          SEMI_MAJOR_AXIS,
          smaFromVelocityAndAltitude(
              keplerian.getDataFor(VELOCITY_PERIAPSIS), keplerian.getDataFor(PERIAPSIS)));

      return keplerian;
    } else {
      keplerian.setDataFor(
          SEMI_MAJOR_AXIS,
          smaFromVelocityAndAltitude(
              keplerian.getDataFor(VELOCITY_APOAPSIS), keplerian.getDataFor(APOAPSIS)));

      return keplerian;
    }
  }

  public static Keplerian calculateAltitudeFromVelocityAndSMA(
      Keplerian keplerian, boolean periapsis) {

    if (periapsis) {
      keplerian.setDataFor(
          PERIAPSIS,
          altitudeFromVelocityAndSMA(
              keplerian.getDataFor(VELOCITY_PERIAPSIS), keplerian.getDataFor(SEMI_MAJOR_AXIS)));

      return keplerian;
    } else {
      keplerian.setDataFor(
          APOAPSIS,
          altitudeFromVelocityAndSMA(
              keplerian.getDataFor(VELOCITY_APOAPSIS), keplerian.getDataFor(SEMI_MAJOR_AXIS)));

      return keplerian;
    }
  }

  public static Keplerian calculateBothVelocities(Keplerian keplerian) {
    double altitudePE = keplerian.getDataFor(PERIAPSIS);
    double altitudeAP = keplerian.getDataFor(APOAPSIS);
    double sma = keplerian.getDataFor(SEMI_MAJOR_AXIS);
    double velocityAP = velocityFromAltitudeAndSMA(altitudeAP, sma);
    double velocityPE = velocityFromAltitudeAndSMA(altitudePE, sma);
    keplerian.setDataFor(VELOCITY_APOAPSIS, velocityAP);
    keplerian.setDataFor(VELOCITY_PERIAPSIS, velocityPE);
    return keplerian;
  }

  public static double velocityFromAltitudeAndSMA(double altitude, double sma) {
    double radius = addRadiusOfBody(altitude);
    return Math.sqrt(body.getMu() * ((2 / radius) - (1 / sma)));
  }

  private static double addRadiusOfBody(double altitude) {
    return altitude += body.getRadius();
  }

  public static double smaFromVelocityAndAltitude(double velocity, double altitude) {
    double radius = addRadiusOfBody(altitude);
    return 1 / ((2 / radius) - ((velocity * velocity) / body.getMu()));
  }

  public static double altitudeFromVelocityAndSMA(double velocity, double sma) {
    double radius = 2 / (((velocity * velocity) / body.getMu()) + (1 / sma));
    radius -= body.getRadius();
    return radius;
  }
}
