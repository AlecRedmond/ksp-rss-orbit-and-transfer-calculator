package org.artools.orbitcalculator.method.kepler;

import static java.lang.Math.*;
import static org.apache.commons.math3.util.FastMath.atan2;
import static org.apache.commons.math3.util.MathUtils.TWO_PI;
import static org.apache.commons.math3.util.MathUtils.normalizeAngle;
import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;

import java.util.Optional;
import java.util.Set;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;

public class KeplerUtils {
  public static final Set<KeplerElement> ELLIPTICAL_ELEMENTS =
      Set.of(ECCENTRICITY, SEMI_MAJOR_AXIS, APOAPSIS, PERIAPSIS, ORBITAL_PERIOD);
  public static final Set<KeplerElement> ROTATIONAL_ELEMENTS =
      Set.of(LONGITUDE_ASCENDING_NODE, INCLINATION, ARGUMENT_OF_PERIAPSIS);
  public static final Set<KeplerElement> POSITION_ELEMENTS =
      Set.of(MEAN_ANOMALY, ECCENTRIC_ANOMALY, TRUE_ANOMALY, TIME_TO_PERIAPSIS);
  public static final Set<KeplerElement> ANGULAR_ELEMENTS =
      Set.of(
          INCLINATION,
          LONGITUDE_ASCENDING_NODE,
          ARGUMENT_OF_PERIAPSIS,
          MEAN_ANOMALY,
          ECCENTRIC_ANOMALY,
          TRUE_ANOMALY);
  private static final double ROOT_FINDER_TOLERANCE = 1e-9;

  private KeplerUtils() {}

  public static void calculateElement(
      KeplerOrbit orbit, KeplerElement element, Planet centralBody) {
    if (orbit.containsKey(element)) return;
    switch (element) {
      case ECCENTRICITY -> calculateEccentricity(orbit, centralBody);
      case SEMI_MAJOR_AXIS -> calculateSemiMajorAxis(orbit, centralBody);
      case APOAPSIS -> calculateApoapsis(orbit, centralBody);
      case PERIAPSIS -> calculatePeriapsis(orbit, centralBody);
      case ORBITAL_PERIOD -> calculateOrbitalPeriod(orbit, centralBody);
      case MEAN_ANOMALY -> calculateMeanAnomaly(orbit, centralBody);
      case ECCENTRIC_ANOMALY -> calculateEccentricAnomaly(orbit, centralBody);
      case TRUE_ANOMALY -> calculateTrueAnomaly(orbit, centralBody);
      case TIME_TO_PERIAPSIS -> calculateTimeToPeriapsis(orbit, centralBody);
      case INCLINATION, LONGITUDE_ASCENDING_NODE, ARGUMENT_OF_PERIAPSIS -> {
        // will be the same
      }
    }
  }

  public static void calculateSemiMajorAxis(KeplerOrbit orbit, Planet centralBody) {
    if (orbit.containsKey(ORBITAL_PERIOD)) {
      double period = orbit.getData(ORBITAL_PERIOD);
      double mu = centralBody.getMu();
      double semiMajorAxis = pow(((period * sqrt(mu)) / (2 * PI)), (2.0 / 3.0));
      orbit.setData(SEMI_MAJOR_AXIS, semiMajorAxis);
      return;
    }

    double radius = centralBody.getBodyRadius();
    Optional<Double> apoapsisOptional = Optional.ofNullable(orbit.getData(APOAPSIS));
    Optional<Double> periapsisOptional = Optional.ofNullable(orbit.getData(PERIAPSIS));

    if (!orbit.containsKey(ECCENTRICITY)) {
      double apoapsis = apoapsisOptional.orElseThrow();
      double periapsis = periapsisOptional.orElseThrow();
      double semiMajorAxis = (apoapsis + periapsis + 2 * radius) / 2;
      orbit.setData(SEMI_MAJOR_AXIS, semiMajorAxis);
      return;
    }
    double eccentricity = orbit.getData(ECCENTRICITY);
    double semiMajorAxis =
        apoapsisOptional
            .map(apoapsis -> (apoapsis + radius) / (1 + eccentricity))
            .orElseGet(() -> (periapsisOptional.orElseThrow() + radius) / (1 - eccentricity));

    orbit.setData(SEMI_MAJOR_AXIS, semiMajorAxis);
  }

  public static void calculateApoapsis(KeplerOrbit orbit, Planet centralBody) {
    if (!orbit.containsKey(SEMI_MAJOR_AXIS)) calculateElement(orbit, SEMI_MAJOR_AXIS, centralBody);
    if (!orbit.containsKey(ECCENTRICITY)) calculateElement(orbit, ECCENTRICITY, centralBody);
    double radius = centralBody.getBodyRadius();
    double eccentricity = orbit.getData(ECCENTRICITY);
    double semiMajorAxis = orbit.getData(SEMI_MAJOR_AXIS);
    double apoapsis = semiMajorAxis * (1 + eccentricity) - radius;
    orbit.setData(APOAPSIS, apoapsis);
  }

  public static void calculatePeriapsis(KeplerOrbit orbit, Planet centralBody) {
    if (!orbit.containsKey(SEMI_MAJOR_AXIS)) calculateElement(orbit, SEMI_MAJOR_AXIS, centralBody);
    if (!orbit.containsKey(ECCENTRICITY)) calculateElement(orbit, ECCENTRICITY, centralBody);
    double radius = centralBody.getBodyRadius();
    double eccentricity = orbit.getData(ECCENTRICITY);
    double semiMajorAxis = orbit.getData(SEMI_MAJOR_AXIS);
    double periapsis = semiMajorAxis * (1 - eccentricity) - radius;
    orbit.setData(PERIAPSIS, periapsis);
  }

  public static void calculateOrbitalPeriod(KeplerOrbit orbit, Planet centralBody) {
    if (!orbit.containsKey(SEMI_MAJOR_AXIS)) calculateElement(orbit, SEMI_MAJOR_AXIS, centralBody);
    double semiMajorAxis = orbit.getData(SEMI_MAJOR_AXIS);
    double mu = centralBody.getMu();
    double period = (2 * PI) * sqrt(pow(semiMajorAxis, 3) / mu);
    orbit.setData(ORBITAL_PERIOD, period);
  }

  public static void calculateTimeToPeriapsis(KeplerOrbit orbit, Planet centralBody) {
    if (!orbit.containsKey(MEAN_ANOMALY)) calculateMeanAnomaly(orbit, centralBody);
    ensureValuesWithinBounds(MEAN_ANOMALY, orbit);
    double meanAnomaly = orbit.getData(MEAN_ANOMALY);
    double period = orbit.getData(ORBITAL_PERIOD);
    double timeToPeriapsis = period * ((TWO_PI - meanAnomaly) / TWO_PI);
    orbit.setData(TIME_TO_PERIAPSIS, timeToPeriapsis);
  }

  public static void ensureValuesWithinBounds(KeplerElement element, KeplerOrbit orbit) {
    if (TIME_TO_PERIAPSIS.equals(element)) {
      setTimeToPeWithinBounds(orbit);
    }
    if (ANGULAR_ELEMENTS.contains(element)) {
      double value = orbit.getData(element);
      if (value >= 0 && value < TWO_PI) return;
      double newValue = normalizeAngle(value, PI);
      orbit.setData(element, newValue);
    }
  }

  private static void setTimeToPeWithinBounds(KeplerOrbit orbit) {
    double timeToPe = orbit.getData(TIME_TO_PERIAPSIS);
    double orbitalPeriod = orbit.getData(ORBITAL_PERIOD);
    if (timeToPe >= 0 && timeToPe < orbitalPeriod) return;
    double moduloTimeToPe = timeToPe % orbitalPeriod;
    if (moduloTimeToPe < 0) moduloTimeToPe += orbitalPeriod;
    orbit.setData(TIME_TO_PERIAPSIS, moduloTimeToPe);
  }

  private static void calculateTrueAnomaly(KeplerOrbit orbit, Planet centralBody) {
    if (!orbit.containsKey(ECCENTRIC_ANOMALY))
      calculateElement(orbit, ECCENTRIC_ANOMALY, centralBody);
    double eccentricity = orbit.getData(ECCENTRICITY);
    double eccentricAnomaly = orbit.getData(ECCENTRIC_ANOMALY);
    double beta = eccentricity / (1 + sqrt(1 - pow(eccentricity, 2)));
    double trueAnomaly =
        eccentricAnomaly
            + 2 * atan2(beta * sin(eccentricAnomaly), 1 - beta * cos(eccentricAnomaly));
    trueAnomaly = normalizeAngle(trueAnomaly, PI);
    orbit.setData(TRUE_ANOMALY, trueAnomaly);
  }

  private static void calculateEccentricAnomaly(KeplerOrbit orbit, Planet centralBody) {
    if (orbit.containsKey(TRUE_ANOMALY)) {
      double eccentricity = orbit.getData(ECCENTRICITY);
      double trueAnomaly = orbit.getData(TRUE_ANOMALY);
      double sinE = Math.sqrt(1 - Math.pow(eccentricity, 2)) * Math.sin(trueAnomaly);
      double cosE = eccentricity + Math.cos(trueAnomaly);
      double eAnomaly = atan2(sinE, cosE);
      eAnomaly = normalizeAngle(eAnomaly, PI);
      orbit.setData(ECCENTRIC_ANOMALY, eAnomaly);
      return;
    }
    if (orbit.containsKey(TIME_TO_PERIAPSIS)) calculateElement(orbit, MEAN_ANOMALY, centralBody);
    double meanAnomaly = orbit.getData(MEAN_ANOMALY);
    double eccentricity = orbit.getData(ECCENTRICITY);
    double eccentricAnomaly = newtonRaphsonRootFinder(meanAnomaly, eccentricity, meanAnomaly);
    orbit.setData(ECCENTRIC_ANOMALY, eccentricAnomaly);
  }

  private static double newtonRaphsonRootFinder(
      double trueMeanAnomaly, double eccentricity, double initialEccAnomalyGuess) {
    double calculatedMeanAnomaly = Double.MAX_VALUE;
    double estimatedEccAnomaly = initialEccAnomalyGuess;
    while (abs(trueMeanAnomaly - calculatedMeanAnomaly) > ROOT_FINDER_TOLERANCE) {
      double delta =
          (trueMeanAnomaly - estimatedEccAnomaly + eccentricity * sin(estimatedEccAnomaly))
              / (1 - eccentricity * cos(estimatedEccAnomaly));
      estimatedEccAnomaly += delta;
      calculatedMeanAnomaly = estimatedEccAnomaly - eccentricity * sin(estimatedEccAnomaly);
      calculatedMeanAnomaly = normalizeAngle(calculatedMeanAnomaly, trueMeanAnomaly);
    }
    return normalizeAngle(estimatedEccAnomaly, PI);
  }

  private static void calculateMeanAnomaly(KeplerOrbit orbit, Planet centralBody) {
    if (orbit.containsKey(TIME_TO_PERIAPSIS)) {
      ensureValuesWithinBounds(TIME_TO_PERIAPSIS, orbit);
      double timeToPeriapsis = orbit.getData(TIME_TO_PERIAPSIS);
      double period = orbit.getData(ORBITAL_PERIOD);
      double meanAnomaly = (1 - timeToPeriapsis / period) * TWO_PI;
      meanAnomaly = normalizeAngle(meanAnomaly, PI);
      orbit.setData(MEAN_ANOMALY, meanAnomaly);
      return;
    }
    if (orbit.containsKey(TRUE_ANOMALY)) {
      calculateElement(orbit, ECCENTRIC_ANOMALY, centralBody);
    }
    double eccentricAnomaly = orbit.getData(ECCENTRIC_ANOMALY);
    double eccentricity = orbit.getData(ECCENTRICITY);
    double meanAnomaly = eccentricAnomaly - eccentricity * sin(eccentricAnomaly);
    meanAnomaly = normalizeAngle(meanAnomaly, PI);
    orbit.setData(MEAN_ANOMALY, meanAnomaly);
  }

  private static void calculateEccentricity(KeplerOrbit orbit, Planet centralBody) {
    if (!orbit.containsKey(SEMI_MAJOR_AXIS)) calculateSemiMajorAxis(orbit, centralBody);

    double semiMajorAxis = orbit.getData(SEMI_MAJOR_AXIS);

    Optional<Double> apoapsisOptional = Optional.ofNullable(orbit.getData(APOAPSIS));
    double radius = centralBody.getBodyRadius();

    double eccentricity;
    if (apoapsisOptional.isPresent()) {
      double apoapsis = apoapsisOptional.get() + radius;
      eccentricity = (apoapsis / semiMajorAxis) - 1;
    } else {
      double periapsis = Optional.ofNullable(orbit.getData(PERIAPSIS)).orElseThrow() + radius;
      eccentricity = 1 - (periapsis / semiMajorAxis);
    }
    orbit.setData(ECCENTRICITY, eccentricity);
  }
}
