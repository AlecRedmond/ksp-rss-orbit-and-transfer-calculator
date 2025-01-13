package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import org.example.equations.application.Body;
import org.example.equations.application.Orbit;

public class FillEquations {
  private static Body body = Body.EARTH;

  private FillEquations() {
  }

  public static Orbit calculateFromPeriapsisApoapsis(Orbit orbit) {
    FillEquations.body = orbit.getBody();
    double apoapsis = orbit.getDataFor(APOAPSIS);
    double periapsis = orbit.getDataFor(PERIAPSIS);
    Body body = orbit.getBody();

    if (apoapsis < periapsis) {
      orbit.setDataFor(PERIAPSIS, apoapsis);
      orbit.setDataFor(APOAPSIS, periapsis);
      apoapsis = orbit.getDataFor(APOAPSIS);
      periapsis = orbit.getDataFor(PERIAPSIS);
    }

    apoapsis += body.getRadius();
    periapsis += body.getRadius();

    orbit.setDataFor(SEMI_MAJOR_AXIS, (apoapsis + periapsis) / 2);
    orbit.setDataFor(ECCENTRICITY, (apoapsis - periapsis) / (apoapsis + periapsis));
    orbit = convertSemiMajorAxisToOrbitalPeriod(orbit);
    orbit = calculateBothVelocities(orbit);
    return orbit;
  }

  public static Orbit calculateFromApsisEccentricity(Orbit orbit, boolean hasPeriapsis) {
    FillEquations.body = orbit.getBody();
    double apoapsis;
    double periapsis;
    double eccentricity = orbit.getDataFor(ECCENTRICITY);
    double semiMajorAxis;
    if (hasPeriapsis) {
      periapsis = orbit.getDataFor(PERIAPSIS) + orbit.getBody().getRadius();
      semiMajorAxis = periapsis / (1 - eccentricity);
      orbit.setDataFor(SEMI_MAJOR_AXIS, semiMajorAxis);
    } else {
      apoapsis = orbit.getDataFor(APOAPSIS) + orbit.getBody().getRadius();
      semiMajorAxis = apoapsis / (1 + eccentricity);
      orbit.setDataFor(SEMI_MAJOR_AXIS, semiMajorAxis);
    }
    return calculateFromEccentricitySemiMajorAxis(orbit);
  }

  public static Orbit calculateFromApsisSemiMajorAxis(Orbit orbit, boolean hasPeriapsis) {
    FillEquations.body = orbit.getBody();
    double apoapsis;
    double periapsis;
    double semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    if (hasPeriapsis) {
      periapsis = orbit.getDataFor(PERIAPSIS) + orbit.getBody().getRadius();
      orbit.setDataFor(ECCENTRICITY, 1 - (periapsis / semiMajorAxis));
    } else {
      apoapsis = orbit.getDataFor(APOAPSIS) + orbit.getBody().getRadius();
      orbit.setDataFor(ECCENTRICITY, (apoapsis / semiMajorAxis) - 1);
    }
    return calculateFromEccentricitySemiMajorAxis(orbit);
  }

  public static Orbit calculateFromEccentricitySemiMajorAxis(Orbit orbit) {
    FillEquations.body = orbit.getBody();
    double eccentricity = orbit.getDataFor(ECCENTRICITY);
    double semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    orbit.setDataFor(
        APOAPSIS, (semiMajorAxis * (1 + eccentricity)) - orbit.getBody().getRadius());
    orbit.setDataFor(
        PERIAPSIS, (semiMajorAxis * (1 - eccentricity)) - orbit.getBody().getRadius());
    orbit = convertSemiMajorAxisToOrbitalPeriod(orbit);
    orbit = calculateBothVelocities(orbit);
    return orbit;
  }

  public static Orbit convertOrbitalPeriodToSMA(Orbit orbit) {
    FillEquations.body = orbit.getBody();
    double orbitalPeriod = orbit.getDataFor(ORBITAL_PERIOD);
    double mu = orbit.getBody().getMu();
    double semiMajorAxis =
        Math.pow((orbitalPeriod * Math.sqrt(mu)) / (2 * Math.PI), ((double) 2 / 3));
    orbit.setDataFor(SEMI_MAJOR_AXIS, semiMajorAxis);
    return orbit;
  }

  public static Orbit convertSemiMajorAxisToOrbitalPeriod(Orbit orbit) {
    FillEquations.body = orbit.getBody();
    double semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    double mu = orbit.getBody().getMu();
    double orbitalPeriod = (2 * Math.PI) * Math.sqrt((Math.pow(semiMajorAxis, 3)) / mu);
    orbit.setDataFor(ORBITAL_PERIOD, orbitalPeriod);
    return orbit;
  }

  public static Orbit calculateSMAFromVelocityAndAltitude(
          Orbit orbit, boolean periapsis) {
    FillEquations.body = orbit.getBody();

    if (periapsis) {
      orbit.setDataFor(
          SEMI_MAJOR_AXIS,
          smaFromVelocityAndAltitude(
              orbit.getDataFor(VELOCITY_PERIAPSIS), orbit.getDataFor(PERIAPSIS)));

    } else {
      orbit.setDataFor(
          SEMI_MAJOR_AXIS,
          smaFromVelocityAndAltitude(
              orbit.getDataFor(VELOCITY_APOAPSIS), orbit.getDataFor(APOAPSIS)));

    }
      return orbit;
  }

  public static Orbit calculateAltitudeFromVelocityAndSMA(
          Orbit orbit, boolean periapsis) {
    FillEquations.body = orbit.getBody();

    if (periapsis) {
      orbit.setDataFor(
          PERIAPSIS,
          altitudeFromVelocityAndSMA(
              orbit.getDataFor(VELOCITY_PERIAPSIS), orbit.getDataFor(SEMI_MAJOR_AXIS)));

    } else {
      orbit.setDataFor(
          APOAPSIS,
          altitudeFromVelocityAndSMA(
              orbit.getDataFor(VELOCITY_APOAPSIS), orbit.getDataFor(SEMI_MAJOR_AXIS)));

    }
      return orbit;
  }

  public static Orbit calculateBothVelocities(Orbit orbit) {
    FillEquations.body = orbit.getBody();
    double altitudePE = orbit.getDataFor(PERIAPSIS);
    double altitudeAP = orbit.getDataFor(APOAPSIS);
    double sma = orbit.getDataFor(SEMI_MAJOR_AXIS);
    double velocityAP = velocityFromAltitudeAndSMA(altitudeAP, sma);
    double velocityPE = velocityFromAltitudeAndSMA(altitudePE, sma);
    orbit.setDataFor(VELOCITY_APOAPSIS, velocityAP);
    orbit.setDataFor(VELOCITY_PERIAPSIS, velocityPE);
    return orbit;
  }

  public static double velocityFromAltitudeAndSMA(double altitude, double sma) {
    double radius = addRadiusOfBody(altitude);
    return Math.sqrt(body.getMu() * ((2 / radius) - (1 / sma)));
  }

  private static double addRadiusOfBody(double altitude) {
    altitude += body.getRadius();
    return altitude;
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
