package org.artools.orbitcalculator.method.writeableorbit;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artools.orbitcalculator.application.bodies.AstralBodies;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.application.writeableorbit.keplerianelements.Kepler;

@Data
@Slf4j
@NoArgsConstructor
public class OrbitBuilder {
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
      log.warn(exception.getLocalizedMessage());
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
    if (held(Kepler.KeplerEnums.ORBITAL_PERIOD)) {
      TrajectoryEquations.convertOrbitalPeriodToSMA(orbit);
      orbitalParameterHolds.setHold(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, true);
    }
  }

  private boolean checkHeldApses() throws IncorrectHoldException {
    if (held(Kepler.KeplerEnums.APOAPSIS) || held(Kepler.KeplerEnums.PERIAPSIS)) {
      if (held(Kepler.KeplerEnums.APOAPSIS) && held(Kepler.KeplerEnums.PERIAPSIS)) {
        TrajectoryEquations.calculateFromPeriapsisApoapsis(orbit);
      } else if (held(Kepler.KeplerEnums.ECCENTRICITY)) {
        TrajectoryEquations.calculateFromApsisEccentricity(orbit, held(Kepler.KeplerEnums.PERIAPSIS));
      } else if (held(Kepler.KeplerEnums.SEMI_MAJOR_AXIS)) {
        TrajectoryEquations.calculateFromApsisSemiMajorAxis(orbit, held(Kepler.KeplerEnums.PERIAPSIS));
      } else if (held(Kepler.KeplerEnums.VELOCITY_PERIAPSIS) && held(Kepler.KeplerEnums.PERIAPSIS)
          || held(Kepler.KeplerEnums.VELOCITY_APOAPSIS) && held(Kepler.KeplerEnums.APOAPSIS)) {
        TrajectoryEquations.calculateSMAFromVelocityAndAltitude(orbit, held(Kepler.KeplerEnums.PERIAPSIS));
        TrajectoryEquations.calculateFromApsisSemiMajorAxis(orbit, held(Kepler.KeplerEnums.PERIAPSIS));
      } else {
        throw new IncorrectHoldException("Unable To Build from Apses!!");
      }
      return true;
    }
    return false;
  }

  private boolean checkHeldEccentricity() throws IncorrectHoldException {
    if (held(Kepler.KeplerEnums.ECCENTRICITY)) {
      if (held(Kepler.KeplerEnums.SEMI_MAJOR_AXIS)) {
        TrajectoryEquations.calculateFromEccentricitySemiMajorAxis(orbit);
      } else {
        throw new IncorrectHoldException("Unable To Build from Eccentricity!!");
      }
      return true;
    }
    return false;
  }

  private boolean checkHeldSemiMajorAxis() throws IncorrectHoldException {
    if (held(Kepler.KeplerEnums.SEMI_MAJOR_AXIS)) {
      if (held(Kepler.KeplerEnums.VELOCITY_APOAPSIS) || held(Kepler.KeplerEnums.VELOCITY_PERIAPSIS)) {
        TrajectoryEquations.calculateAltitudeFromVelocityAndSMA(orbit, held(Kepler.KeplerEnums.VELOCITY_PERIAPSIS));
        TrajectoryEquations.calculateFromApsisSemiMajorAxis(orbit, held(Kepler.KeplerEnums.VELOCITY_PERIAPSIS));
      } else {
        throw new IncorrectHoldException("Unable To Build from Semi-Major Axis!!");
      }
      return true;
    }
    return false;
  }

  private void setAllToZero() {
    for (Map.Entry<Kepler.KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      entry.getValue().setData(0.0);
    }
  }

  private int countHolds(OrbitalParameterHolds orbitalParameterHolds) {
    return (int)
        orbitalParameterHolds.getHoldsMap().entrySet().stream()
            .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
            .count();
  }

  private boolean held(Kepler.KeplerEnums keplerEnums) {
    return orbitalParameterHolds.getHold(keplerEnums);
  }

  public OrbitBuilder(double periapsis, double apoapsis, double inclinationDegrees) {
    double inclination = Math.toRadians(inclinationDegrees);
    buildFromApses(periapsis, apoapsis);
    orbit.setDataFor(Kepler.KeplerEnums.INCLINATION, inclination);
  }

  private void buildFromApses(double periapsis, double apoapsis) {
    orbit = new Orbit();
    if (periapsis > apoapsis) {
      double temp = periapsis;
      periapsis = apoapsis;
      apoapsis = temp;
    }
    orbit.setDataFor(Kepler.KeplerEnums.PERIAPSIS, periapsis);
    orbit.setDataFor(Kepler.KeplerEnums.APOAPSIS, apoapsis);
    orbitalParameterHolds = new OrbitalParameterHolds(Kepler.KeplerEnums.PERIAPSIS, Kepler.KeplerEnums.APOAPSIS);
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
    orbit.setDataFor(Kepler.KeplerEnums.RIGHT_ASCENSION, rightAscension);
    orbit.setDataFor(Kepler.KeplerEnums.INCLINATION, inclination);
    orbit.setDataFor(Kepler.KeplerEnums.ARGUMENT_PE, argumentPE);
  }

  public OrbitBuilder buildFromHorizonsData(
      double sma,
      double e,
      double rightAscensionDegs,
      double inclinationDegs,
      double argumentPEdegs,
      AstralBodies astralBodies) {
    orbit = new Orbit(astralBodies);
    orbit.setDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, sma);
    orbit.setDataFor(Kepler.KeplerEnums.ECCENTRICITY, e);
    orbitalParameterHolds = new OrbitalParameterHolds(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, Kepler.KeplerEnums.ECCENTRICITY);
    methodFromHolds();
    orbit.setDataFor(Kepler.KeplerEnums.RIGHT_ASCENSION, Math.toRadians(rightAscensionDegs));
    orbit.setDataFor(Kepler.KeplerEnums.INCLINATION, Math.toRadians(inclinationDegs));
    orbit.setDataFor(Kepler.KeplerEnums.ARGUMENT_PE, Math.toRadians(argumentPEdegs));
    return this;
  }

  public OrbitBuilder buildFromVectors(OrbitalState orbitalVectors) {
    orbit = new Orbit(orbitalVectors.getCentralAstralBodies());
    orbit.setDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, orbitalVectors.getSemiMajorAxis());
    orbit.setDataFor(Kepler.KeplerEnums.ECCENTRICITY, orbitalVectors.getEccentricity().getNorm());
    orbitalParameterHolds = new OrbitalParameterHolds(Kepler.KeplerEnums.SEMI_MAJOR_AXIS, Kepler.KeplerEnums.ECCENTRICITY);
    methodFromHolds();
    orbit.setDataFor(Kepler.KeplerEnums.RIGHT_ASCENSION, orbitalVectors.getRightAscension());
    orbit.setDataFor(Kepler.KeplerEnums.INCLINATION, orbitalVectors.getInclination());
    orbit.setDataFor(Kepler.KeplerEnums.ARGUMENT_PE, orbitalVectors.getArgumentPE());
    return this;
  }

  public static class IncorrectHoldException extends Exception {
    public IncorrectHoldException(String errorMessage) {
      super(errorMessage);
    }
  }
}
