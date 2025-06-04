package org.artools.orbitcalculator.method.writeableorbit;

import org.artools.orbitcalculator.application.bodies.Body;
import org.artools.orbitcalculator.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.application.writeableorbit.keplerianelements.Kepler;

public class TrajectoryEquations {
  private static Body body = Body.EARTH;

  private TrajectoryEquations() {}

  public static void calculateFromPeriapsisApoapsis(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double apoapsis = orbit.getDataFor(Kepler.KeplerEnums.APOAPSIS);
    double periapsis = orbit.getDataFor(Kepler.KeplerEnums.PERIAPSIS);
    Body body = orbit.getBody();

    if (apoapsis < periapsis) {
      orbit.setDataFor(Kepler.KeplerEnums.PERIAPSIS, apoapsis);
      orbit.setDataFor(Kepler.KeplerEnums.APOAPSIS, periapsis);
      apoapsis = orbit.getDataFor(Kepler.KeplerEnums.APOAPSIS);
      periapsis = orbit.getDataFor(Kepler.KeplerEnums.PERIAPSIS);
    }

    apoapsis += body.getRadius();
    periapsis += body.getRadius();

    orbit.setDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, (apoapsis + periapsis) / 2);
    orbit.setDataFor(Kepler.KeplerEnums.ECCENTRICITY, (apoapsis - periapsis) / (apoapsis + periapsis));
    convertSemiMajorAxisToOrbitalPeriod(orbit);
    calculateBothVelocities(orbit);
  }

  public static void convertSemiMajorAxisToOrbitalPeriod(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double semiMajorAxis = orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS);
    double mu = orbit.getBody().getMu();
    double orbitalPeriod = (2 * Math.PI) * Math.sqrt((Math.pow(semiMajorAxis, 3)) / mu);
    orbit.setDataFor(Kepler.KeplerEnums.ORBITAL_PERIOD, orbitalPeriod);
  }

  public static void calculateBothVelocities(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double altitudePE = orbit.getDataFor(Kepler.KeplerEnums.PERIAPSIS);
    double altitudeAP = orbit.getDataFor(Kepler.KeplerEnums.APOAPSIS);
    double sma = orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS);
    double velocityAP = velocityFromAltitudeAndSMA(altitudeAP, sma);
    double velocityPE = velocityFromAltitudeAndSMA(altitudePE, sma);
    orbit.setDataFor(Kepler.KeplerEnums.VELOCITY_APOAPSIS, velocityAP);
    orbit.setDataFor(Kepler.KeplerEnums.VELOCITY_PERIAPSIS, velocityPE);
  }

  public static double velocityFromAltitudeAndSMA(double altitude, double sma) {
    double radius = addRadiusOfBody(altitude);
    return Math.sqrt(body.getMu() * ((2 / radius) - (1 / sma)));
  }

  private static double addRadiusOfBody(double altitude) {
    altitude += body.getRadius();
    return altitude;
  }

  public static void calculateFromApsisEccentricity(Orbit orbit, boolean hasPeriapsis) {
    TrajectoryEquations.body = orbit.getBody();
    double apoapsis;
    double periapsis;
    double eccentricity = orbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY);
    double semiMajorAxis;
    if (hasPeriapsis) {
      periapsis = orbit.getDataFor(Kepler.KeplerEnums.PERIAPSIS) + orbit.getBody().getRadius();
      semiMajorAxis = periapsis / (1 - eccentricity);
      orbit.setDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, semiMajorAxis);
    } else {
      apoapsis = orbit.getDataFor(Kepler.KeplerEnums.APOAPSIS) + orbit.getBody().getRadius();
      semiMajorAxis = apoapsis / (1 + eccentricity);
      orbit.setDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, semiMajorAxis);
    }
    calculateFromEccentricitySemiMajorAxis(orbit);
  }

  public static void calculateFromEccentricitySemiMajorAxis(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double eccentricity = orbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY);
    double semiMajorAxis = orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS);
    orbit.setDataFor(Kepler.KeplerEnums.APOAPSIS, (semiMajorAxis * (1 + eccentricity)) - orbit.getBody().getRadius());
    orbit.setDataFor(Kepler.KeplerEnums.PERIAPSIS, (semiMajorAxis * (1 - eccentricity)) - orbit.getBody().getRadius());
    convertSemiMajorAxisToOrbitalPeriod(orbit);
    calculateBothVelocities(orbit);
  }

  public static void calculateFromApsisSemiMajorAxis(Orbit orbit, boolean hasPeriapsis) {
    TrajectoryEquations.body = orbit.getBody();
    double apoapsis;
    double periapsis;
    double semiMajorAxis = orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS);
    if (hasPeriapsis) {
      periapsis = orbit.getDataFor(Kepler.KeplerEnums.PERIAPSIS) + orbit.getBody().getRadius();
      orbit.setDataFor(Kepler.KeplerEnums.ECCENTRICITY, 1 - (periapsis / semiMajorAxis));
    } else {
      apoapsis = orbit.getDataFor(Kepler.KeplerEnums.APOAPSIS) + orbit.getBody().getRadius();
      orbit.setDataFor(Kepler.KeplerEnums.ECCENTRICITY, (apoapsis / semiMajorAxis) - 1);
    }
    calculateFromEccentricitySemiMajorAxis(orbit);
  }

  public static void convertOrbitalPeriodToSMA(Orbit orbit) {
    TrajectoryEquations.body = orbit.getBody();
    double orbitalPeriod = orbit.getDataFor(Kepler.KeplerEnums.ORBITAL_PERIOD);
    double mu = orbit.getBody().getMu();
    double semiMajorAxis =
        Math.pow((orbitalPeriod * Math.sqrt(mu)) / (2 * Math.PI), ((double) 2 / 3));
    orbit.setDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, semiMajorAxis);
  }

  public static void calculateSMAFromVelocityAndAltitude(Orbit orbit, boolean periapsis) {
    TrajectoryEquations.body = orbit.getBody();

    if (periapsis) {
      orbit.setDataFor(
          Kepler.KeplerEnums.SEMI_MAJOR_AXIS,
          smaFromVelocityAndAltitude(
              orbit.getDataFor(Kepler.KeplerEnums.VELOCITY_PERIAPSIS), orbit.getDataFor(Kepler.KeplerEnums.PERIAPSIS)));

    } else {
      orbit.setDataFor(
          Kepler.KeplerEnums.SEMI_MAJOR_AXIS,
          smaFromVelocityAndAltitude(
              orbit.getDataFor(Kepler.KeplerEnums.VELOCITY_APOAPSIS), orbit.getDataFor(Kepler.KeplerEnums.APOAPSIS)));
    }
  }

  public static double smaFromVelocityAndAltitude(double velocity, double altitude) {
    double radius = addRadiusOfBody(altitude);
    return 1 / ((2 / radius) - ((velocity * velocity) / body.getMu()));
  }

  public static void calculateAltitudeFromVelocityAndSMA(Orbit orbit, boolean periapsis) {
    TrajectoryEquations.body = orbit.getBody();

    if (periapsis) {
      orbit.setDataFor(
          Kepler.KeplerEnums.PERIAPSIS,
          altitudeFromVelocityAndSMA(
              orbit.getDataFor(Kepler.KeplerEnums.VELOCITY_PERIAPSIS), orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS)));

    } else {
      orbit.setDataFor(
          Kepler.KeplerEnums.APOAPSIS,
          altitudeFromVelocityAndSMA(
              orbit.getDataFor(Kepler.KeplerEnums.VELOCITY_APOAPSIS), orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS)));
    }
  }

  public static double altitudeFromVelocityAndSMA(double velocity, double sma) {
    double radius = 2 / (((velocity * velocity) / body.getMu()) + (1 / sma));
    radius -= body.getRadius();
    return radius;
  }
}
