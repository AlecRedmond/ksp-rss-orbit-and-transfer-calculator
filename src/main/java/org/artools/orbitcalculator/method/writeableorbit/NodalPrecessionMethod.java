package org.artools.orbitcalculator.method.writeableorbit;

import lombok.Data;
import org.artools.orbitcalculator.application.writeableorbit.Orbit;
import org.artools.orbitcalculator.application.writeableorbit.keplerianelements.Kepler;

@Data
public class NodalPrecessionMethod {
  private Orbit orbit;

  public NodalPrecessionMethod(Orbit orbit) {
    this.orbit = orbit;
    double omegaP = precessionMethod(orbit);
    orbit.setDataFor(Kepler.KeplerEnums.NODAL_PRECESSION, omegaP);
  }

  public NodalPrecessionMethod(Orbit orbit, double desiredPrecession) {
    this.orbit = orbit;
    double inclination = precessionFitMethod(orbit, desiredPrecession);
    orbit.setDataFor(Kepler.KeplerEnums.INCLINATION, inclination);
    orbit.setDataFor(Kepler.KeplerEnums.NODAL_PRECESSION, desiredPrecession);
  }

  // https://i.imgur.com/lJ07OIn.png for the formula
  private double precessionMethod(Orbit orbit) {
    double omegaP;
    double radiusBody = orbit.getAstralBodies().getRadius();
    double semiMajorAxis = orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS);
    double eccentricity = orbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY);
    double omegaSat = (2 * Math.PI) / (orbit.getDataFor(Kepler.KeplerEnums.ORBITAL_PERIOD));
    double inclination = orbit.getDataFor(Kepler.KeplerEnums.INCLINATION);
    double j2 = orbit.getAstralBodies().getJ2();

    double equationTopHalf = -3 * radiusBody * radiusBody * j2 * omegaSat * Math.cos(inclination);
    double equationBotHalf = 2 * Math.pow((semiMajorAxis * (1 - (eccentricity * eccentricity))), 2);
    omegaP = equationTopHalf / equationBotHalf;

    // omegaP is in rad/s. Convert to rad/day
    omegaP = omegaP * 3600 * 24;
    return omegaP;
  }

  private double precessionFitMethod(Orbit orbit, double desiredPrecession) {
    double omegaP = desiredPrecession;
    double radiusBody = orbit.getAstralBodies().getRadius();
    double semiMajorAxis = orbit.getDataFor(Kepler.KeplerEnums.SEMI_MAJOR_AXIS);
    double eccentricity = orbit.getDataFor(Kepler.KeplerEnums.ECCENTRICITY);
    double omegaSat = (2 * Math.PI) / (orbit.getDataFor(Kepler.KeplerEnums.ORBITAL_PERIOD));
    double inclination;
    double j2 = orbit.getAstralBodies().getJ2();

    // omegaP is in rad/s. Convert to rad/day
    omegaP = omegaP / (3600 * 24);

    double equationBotHalf = -3 * radiusBody * radiusBody * j2 * omegaSat;
    double equationTopHalf =
        2 * omegaP * Math.pow((semiMajorAxis * (1 - (eccentricity * eccentricity))), 2);
    inclination = equationTopHalf / equationBotHalf;

    return Math.acos(inclination);
  }
}
