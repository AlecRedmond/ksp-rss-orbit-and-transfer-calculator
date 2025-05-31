package org.artools.orbitcalculator.orbitcalculation.method.writeableorbit;

import static org.artools.orbitcalculator.orbitcalculation.application.writeableorbit.keplerianelements.Kepler.KeplerEnums.*;

import java.util.Map;
import java.util.logging.Logger;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.orbitcalculation.application.Body;
import org.artools.orbitcalculator.orbitcalculation.application.vector.OrbitalVectors;
import org.artools.orbitcalculator.orbitcalculation.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.orbitcalculation.application.writeableorbit.keplerianelements.Kepler;
import org.artools.orbitcalculator.orbitcalculation.application.writeableorbit.keplerianelements.Kepler.KeplerEnums;

@Data
@NoArgsConstructor
public class OrbitBuilder {
  private static final Logger LOG = Logger.getLogger(OrbitBuilder.class.getName());
  private Orbit orbit;
  private OrbitalParameterHolds orbitalParameterHolds;

  public OrbitBuilder(Orbit orbit, OrbitalParameterHolds orbitalParameterHolds) {
    this.orbit = orbit;
    this.orbitalParameterHolds = orbitalParameterHolds;
    methodFromHolds();
  }

  private void methodFromHolds() {
    try {
      checkTwoHolds();
      checkHeldPeriod();
      if (checkHeldApses()) return;
      if (checkHeldEccentricity()) return;
      if (checkHeldSemiMajorAxis()) return;
      throw new IncorrectHoldException("Unable To Build from Any Holds!!");
    } catch (IncorrectHoldException exception) {
      LOG.warning(exception.getLocalizedMessage());
      setAllToZero();
    }
  }

  private void checkTwoHolds() throws IncorrectHoldException {
    int holdsPressed = countHolds(orbitalParameterHolds);
    if (holdsPressed != 2) {
      throw new IncorrectHoldException("More than Two Held Values!");
    }
  }

  private void checkHeldPeriod() {
    if (held(ORBITAL_PERIOD)) {
      TrajectoryEquations.convertOrbitalPeriodToSMA(orbit);
      orbitalParameterHolds.setHold(SEMI_MAJOR_AXIS, true);
    }
  }

  private boolean checkHeldApses() throws IncorrectHoldException {
    if (held(APOAPSIS) || held(PERIAPSIS)) {
      if (held(APOAPSIS) && held(PERIAPSIS)) {
        TrajectoryEquations.calculateFromPeriapsisApoapsis(orbit);
      } else if (held(ECCENTRICITY)) {
        TrajectoryEquations.calculateFromApsisEccentricity(orbit, held(PERIAPSIS));
      } else if (held(SEMI_MAJOR_AXIS)) {
        TrajectoryEquations.calculateFromApsisSemiMajorAxis(orbit, held(PERIAPSIS));
      } else if (held(VELOCITY_PERIAPSIS) && held(PERIAPSIS)
          || held(VELOCITY_APOAPSIS) && held(APOAPSIS)) {
        TrajectoryEquations.calculateSMAFromVelocityAndAltitude(orbit, held(PERIAPSIS));
        TrajectoryEquations.calculateFromApsisSemiMajorAxis(orbit, held(PERIAPSIS));
      } else {
        throw new IncorrectHoldException("Unable To Build from Apses!!");
      }
      return true;
    }
    return false;
  }

  private boolean checkHeldEccentricity() throws IncorrectHoldException {
    if (held(ECCENTRICITY)) {
      if (held(SEMI_MAJOR_AXIS)) {
        TrajectoryEquations.calculateFromEccentricitySemiMajorAxis(orbit);
      } else {
        throw new IncorrectHoldException("Unable To Build from Eccentricity!!");
      }
      return true;
    }
    return false;
  }

  private boolean checkHeldSemiMajorAxis() throws IncorrectHoldException {
    if (held(SEMI_MAJOR_AXIS)) {
      if (held(VELOCITY_APOAPSIS) || held(VELOCITY_PERIAPSIS)) {
        TrajectoryEquations.calculateAltitudeFromVelocityAndSMA(orbit, held(VELOCITY_PERIAPSIS));
        TrajectoryEquations.calculateFromApsisSemiMajorAxis(orbit, held(VELOCITY_PERIAPSIS));
      } else {
        throw new IncorrectHoldException("Unable To Build from Semi-Major Axis!!");
      }
      return true;
    }
    return false;
  }

  private void setAllToZero() {
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      entry.getValue().setData(0.0);
    }
  }

  private int countHolds(OrbitalParameterHolds orbitalParameterHolds) {
    return (int)
        orbitalParameterHolds.getHoldsMap().entrySet().stream()
            .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
            .count();
  }

  private boolean held(KeplerEnums keplerEnums) {
    return orbitalParameterHolds.getHold(keplerEnums);
  }

  public OrbitBuilder(double periapsis, double apoapsis, double inclinationDegrees) {
    double inclination = Math.toRadians(inclinationDegrees);
    buildFromApses(periapsis, apoapsis);
    orbit.setDataFor(INCLINATION, inclination);
  }

  private void buildFromApses(double periapsis, double apoapsis) {
    orbit = new Orbit();
    if (periapsis > apoapsis) {
      double temp = periapsis;
      periapsis = apoapsis;
      apoapsis = temp;
    }
    orbit.setDataFor(PERIAPSIS, periapsis);
    orbit.setDataFor(APOAPSIS, apoapsis);
    orbitalParameterHolds = new OrbitalParameterHolds(PERIAPSIS, APOAPSIS);
    methodFromHolds();
  }

  public OrbitBuilder(double periapsis, double apoapsis) {
    buildFromApses(periapsis, apoapsis);
  }

  public OrbitBuilder(
      double periapsis,
      double apoapsis,
      double rightAscensionDegrees,
      double inclinationDegrees,
      double argumentPEDegrees) {
    var rightAscension = Math.toRadians(rightAscensionDegrees);
    var inclination = Math.toRadians(inclinationDegrees);
    var argumentPE = Math.toRadians(argumentPEDegrees);
    buildFromApses(periapsis, apoapsis);
    orbit.setDataFor(RIGHT_ASCENSION, rightAscension);
    orbit.setDataFor(INCLINATION, inclination);
    orbit.setDataFor(ARGUMENT_PE, argumentPE);
  }

  public OrbitBuilder buildFromHorizonsData(
      double sma,
      double e,
      double rightAscensionDegs,
      double inclinationDegs,
      double argumentPEdegs,
      Body body) {
    orbit = new Orbit(body);
    orbit.setDataFor(SEMI_MAJOR_AXIS, sma);
    orbit.setDataFor(ECCENTRICITY, e);
    orbitalParameterHolds = new OrbitalParameterHolds(SEMI_MAJOR_AXIS, ECCENTRICITY);
    methodFromHolds();
    orbit.setDataFor(RIGHT_ASCENSION, Math.toRadians(rightAscensionDegs));
    orbit.setDataFor(INCLINATION, Math.toRadians(inclinationDegs));
    orbit.setDataFor(ARGUMENT_PE, Math.toRadians(argumentPEdegs));
    return this;
  }

  public OrbitBuilder buildFromVectors(OrbitalVectors orbitalVectors) {
    orbit = new Orbit(orbitalVectors.getCentralBody());
    orbit.setDataFor(SEMI_MAJOR_AXIS, orbitalVectors.getSemiMajorAxis());
    orbit.setDataFor(ECCENTRICITY, orbitalVectors.getEccentricity().getNorm());
    orbitalParameterHolds = new OrbitalParameterHolds(SEMI_MAJOR_AXIS, ECCENTRICITY);
    methodFromHolds();
    orbit.setDataFor(RIGHT_ASCENSION, orbitalVectors.getRightAscension());
    orbit.setDataFor(INCLINATION, orbitalVectors.getInclination());
    orbit.setDataFor(ARGUMENT_PE, orbitalVectors.getArgumentPE());
    return this;
  }

  public static class IncorrectHoldException extends Exception {
    public IncorrectHoldException(String errorMessage) {
      super(errorMessage);
    }
  }
}
