package org.artools.orbitcalculator.method.kepler;

import static java.lang.Math.*;
import static org.apache.commons.math3.util.FastMath.atan2;
import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;

import java.util.Optional;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;

@NoArgsConstructor
public class KeplerUtils {
  private static final double ROOT_FINDER_TOLERANCE = 1e-9;

  public void calculateElement(KeplerOrbit orbit, KeplerElement element) {
    if (orbit.containsKey(element)) return;
    switch (element) {
      case ECCENTRICITY -> calculateEccentricity(orbit);
      case SEMI_MAJOR_AXIS -> calculateSemiMajorAxis(orbit);
      case APOAPSIS -> calculateApoapsis(orbit);
      case PERIAPSIS -> calculatePeriapsis(orbit);
      case ORBITAL_PERIOD -> calculateOrbitalPeriod(orbit);
      case MEAN_ANOMALY -> calculateMeanAnomaly(orbit);
      case ECCENTRIC_ANOMALY -> calculateEccentricAnomaly(orbit);
      case TRUE_ANOMALY -> calculateTrueAnomaly(orbit);
      case TIME_TO_PERIAPSIS -> calculateTimeToPeriapsis(orbit);
      case INCLINATION, LONGITUDE_ASCENDING_NODE, ARGUMENT_OF_PERIAPSIS -> {
        // will be the same
      }
    }
  }

  public void calculateSemiMajorAxis(KeplerOrbit orbit) {
    if (orbit.containsKey(ORBITAL_PERIOD)) {
      double period = orbit.getData(ORBITAL_PERIOD);
      double mu = orbit.getCentralBody().getMu();
      double semiMajorAxis = pow(((period * sqrt(mu)) / (2 * PI)), (2.0 / 3.0));
      orbit.setData(SEMI_MAJOR_AXIS, semiMajorAxis);
      return;
    }

    double radius = orbit.getCentralBody().getBodyRadius();
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

  public void calculateApoapsis(KeplerOrbit orbit) {
    if (!orbit.containsKey(SEMI_MAJOR_AXIS)) calculateElement(orbit, SEMI_MAJOR_AXIS);
    if (!orbit.containsKey(ECCENTRICITY)) calculateElement(orbit, ECCENTRICITY);
    double radius = orbit.getCentralBody().getBodyRadius();
    double eccentricity = orbit.getData(ECCENTRICITY);
    double semiMajorAxis = orbit.getData(SEMI_MAJOR_AXIS);
    double apoapsis = semiMajorAxis * (1 + eccentricity) - radius;
    orbit.setData(APOAPSIS, apoapsis);
  }

  public void calculatePeriapsis(KeplerOrbit orbit) {
    if (!orbit.containsKey(SEMI_MAJOR_AXIS)) calculateElement(orbit, SEMI_MAJOR_AXIS);
    if (!orbit.containsKey(ECCENTRICITY)) calculateElement(orbit, ECCENTRICITY);
    double radius = orbit.getCentralBody().getBodyRadius();
    double eccentricity = orbit.getData(ECCENTRICITY);
    double semiMajorAxis = orbit.getData(SEMI_MAJOR_AXIS);
    double periapsis = semiMajorAxis * (1 - eccentricity) - radius;
    orbit.setData(PERIAPSIS, periapsis);
  }

  public void calculateOrbitalPeriod(KeplerOrbit orbit) {
    if (!orbit.containsKey(SEMI_MAJOR_AXIS)) calculateElement(orbit, SEMI_MAJOR_AXIS);
    double semiMajorAxis = orbit.getData(SEMI_MAJOR_AXIS);
    double mu = orbit.getCentralBody().getMu();
    double period = (2 * PI) * sqrt(pow(semiMajorAxis, 3) / mu);
    orbit.setData(ORBITAL_PERIOD, period);
  }

  public void calculateTimeToPeriapsis(KeplerOrbit orbit) {
    if (!orbit.containsKey(MEAN_ANOMALY)) calculateMeanAnomaly(orbit);
    double meanAnomaly = orbit.getData(MEAN_ANOMALY);
    double period = orbit.getData(ORBITAL_PERIOD);
    double timeToPeriapsis = period * (2 * PI - meanAnomaly);
    orbit.setData(TIME_TO_PERIAPSIS, timeToPeriapsis);
  }

  public void convertRadialValuesToPositive(KeplerElement element, KeplerOrbit orbit) {
    if (orbit.getData(element) >= 0 || ELLIPTICAL_ELEMENTS.contains(element)) {
      return;
    }
    if (element.equals(TIME_TO_PERIAPSIS)) {
      double oldTimeToPe = orbit.getData(TIME_TO_PERIAPSIS);
      orbit.setData(TIME_TO_PERIAPSIS, -oldTimeToPe);
      return;
    }
    double oldValue = orbit.getData(element);
    double newValue = 2 * PI + oldValue;
    orbit.setData(element, newValue);
  }

  private void calculateTrueAnomaly(KeplerOrbit orbit) {
    if (!orbit.containsKey(ECCENTRIC_ANOMALY)) calculateElement(orbit, ECCENTRIC_ANOMALY);
    double eccentricity = orbit.getData(ECCENTRICITY);
    double eccentricAnomaly = orbit.getData(ECCENTRIC_ANOMALY);
    double beta = eccentricity / (1 + sqrt(1 - pow(eccentricity, 2)));
    double trueAnomaly =
        eccentricAnomaly
            + 2 * atan2(beta * sin(eccentricAnomaly), 1 - beta * cos(eccentricAnomaly));
    if (trueAnomaly < 0) trueAnomaly += 2 * PI;
    orbit.setData(TRUE_ANOMALY, trueAnomaly);
  }

  private void calculateEccentricAnomaly(KeplerOrbit orbit) {
    if (orbit.containsKey(TRUE_ANOMALY)) {
      double eccentricity = orbit.getData(ECCENTRICITY);
      double trueAnomaly = orbit.getData(TRUE_ANOMALY);
      double sinE = Math.sqrt(1 - Math.pow(eccentricity, 2)) * Math.sin(trueAnomaly);
      double cosE = eccentricity + Math.cos(trueAnomaly);
      double eAnomaly = atan2(sinE, cosE);
      eAnomaly = eAnomaly >= 0 ? eAnomaly : 2 * Math.PI - eAnomaly;
      orbit.setData(ECCENTRIC_ANOMALY, eAnomaly);
      return;
    }
    if (orbit.containsKey(TIME_TO_PERIAPSIS)) calculateElement(orbit, MEAN_ANOMALY);
    double meanAnomaly = orbit.getData(MEAN_ANOMALY);
    double eccentricity = orbit.getData(ECCENTRICITY);
    double eccentricAnomaly = newtonRaphsonRootFinder(meanAnomaly, eccentricity, meanAnomaly);
    orbit.setData(ECCENTRIC_ANOMALY, eccentricAnomaly);
  }

  private double newtonRaphsonRootFinder(
      double trueMeanAnomaly, double eccentricity, double initialGuess) {
    double delta =
        (trueMeanAnomaly - initialGuess + eccentricity * sin(initialGuess))
            / (1 - eccentricity * cos(initialGuess));
    double currentGuess = initialGuess + delta;
    double calculatedMeanAnomaly = currentGuess - eccentricity * sin(currentGuess);
    double difference = abs(trueMeanAnomaly - calculatedMeanAnomaly);
    if (difference <= ROOT_FINDER_TOLERANCE) {
      return currentGuess;
    }
    return newtonRaphsonRootFinder(trueMeanAnomaly, eccentricity, currentGuess);
  }

  private void calculateMeanAnomaly(KeplerOrbit orbit) {
    if (orbit.containsKey(TIME_TO_PERIAPSIS)) {
      double timeToPeriapsis = orbit.getData(TIME_TO_PERIAPSIS);
      double period = orbit.getData(ORBITAL_PERIOD);
      double meanAnomaly = (1 - timeToPeriapsis / period) * 2 * PI;
      orbit.setData(MEAN_ANOMALY, meanAnomaly);
      return;
    }
    if (orbit.containsKey(TRUE_ANOMALY)) {
      calculateElement(orbit, ECCENTRIC_ANOMALY);
    }
    double eccentricAnomaly = orbit.getData(ECCENTRIC_ANOMALY);
    double eccentricity = orbit.getData(ECCENTRICITY);
    double meanAnomaly = eccentricAnomaly - eccentricity * sin(eccentricAnomaly);
    orbit.setData(MEAN_ANOMALY, meanAnomaly);
  }

  private void calculateEccentricity(KeplerOrbit orbit) {
    if (!orbit.containsKey(SEMI_MAJOR_AXIS)) calculateSemiMajorAxis(orbit);

    double semiMajorAxis = orbit.getData(SEMI_MAJOR_AXIS);

    Optional<Double> apoapsisOptional = Optional.ofNullable(orbit.getData(APOAPSIS));
    double radius = orbit.getCentralBody().getBodyRadius();

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
