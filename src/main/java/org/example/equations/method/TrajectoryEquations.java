package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import org.example.equations.application.Body;
import org.example.equations.application.Orbit;

public class TrajectoryEquations {
  private static Body body = Body.EARTH;

  private TrajectoryEquations() {
  }

  public static void calculateFromPeriapsisApoapsis(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
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
    convertSemiMajorAxisToOrbitalPeriod(orbit);
    calculateBothVelocities(orbit);
  }

  public static void calculateFromApsisEccentricity(Orbit orbit, boolean hasPeriapsis) {
    TrajectoryEquations.body = orbit.getBody();
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
    calculateFromEccentricitySemiMajorAxis(orbit);
  }

  public static void calculateFromApsisSemiMajorAxis(Orbit orbit, boolean hasPeriapsis) {
    TrajectoryEquations.body = orbit.getBody();
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
    calculateFromEccentricitySemiMajorAxis(orbit);
  }

  public static void calculateFromEccentricitySemiMajorAxis(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double eccentricity = orbit.getDataFor(ECCENTRICITY);
    double semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    orbit.setDataFor(
        APOAPSIS, (semiMajorAxis * (1 + eccentricity)) - orbit.getBody().getRadius());
    orbit.setDataFor(
        PERIAPSIS, (semiMajorAxis * (1 - eccentricity)) - orbit.getBody().getRadius());
    convertSemiMajorAxisToOrbitalPeriod(orbit);
    calculateBothVelocities(orbit);
  }

  public static void convertOrbitalPeriodToSMA(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double orbitalPeriod = orbit.getDataFor(ORBITAL_PERIOD);
    double mu = orbit.getBody().getMu();
    double semiMajorAxis =
        Math.pow((orbitalPeriod * Math.sqrt(mu)) / (2 * Math.PI), ((double) 2 / 3));
    orbit.setDataFor(SEMI_MAJOR_AXIS, semiMajorAxis);
  }

  public static void convertSemiMajorAxisToOrbitalPeriod(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    double mu = orbit.getBody().getMu();
    double orbitalPeriod = (2 * Math.PI) * Math.sqrt((Math.pow(semiMajorAxis, 3)) / mu);
    orbit.setDataFor(ORBITAL_PERIOD, orbitalPeriod);
  }

  public static void calculateSMAFromVelocityAndAltitude(
          Orbit orbit, boolean periapsis) {
    TrajectoryEquations.body = orbit.getBody();

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
  }

  public static void calculateAltitudeFromVelocityAndSMA(
          Orbit orbit, boolean periapsis) {
    TrajectoryEquations.body = orbit.getBody();

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
  }

  public static void calculateBothVelocities(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double altitudePE = orbit.getDataFor(PERIAPSIS);
    double altitudeAP = orbit.getDataFor(APOAPSIS);
    double sma = orbit.getDataFor(SEMI_MAJOR_AXIS);
    double velocityAP = velocityFromAltitudeAndSMA(altitudeAP, sma);
    double velocityPE = velocityFromAltitudeAndSMA(altitudePE, sma);
    orbit.setDataFor(VELOCITY_APOAPSIS, velocityAP);
    orbit.setDataFor(VELOCITY_PERIAPSIS, velocityPE);
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
