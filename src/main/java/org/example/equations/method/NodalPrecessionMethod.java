package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import lombok.Data;
import org.example.equations.application.Orbit;

import java.util.ArrayList;

@Data
public class NodalPrecessionMethod {
  private Orbit orbit;

  public NodalPrecessionMethod(Orbit orbit) {
    this.orbit = orbit;
    double omegaP = precessionMethod(orbit);
    orbit.setDataFor(NODAL_PRECESSION, omegaP);
  }

  public NodalPrecessionMethod(Orbit orbit, double desiredPrecession) {
    this.orbit = orbit;
    double inclination = precessionFitMethod(orbit, desiredPrecession);
    orbit.setDataFor(INCLINATION, inclination);
    orbit.setDataFor(NODAL_PRECESSION, desiredPrecession);
  }

  // https://i.imgur.com/lJ07OIn.png for the formula
  private double precessionMethod(Orbit orbit) {
    double omegaP;
    double radiusBody = orbit.getBody().getRadius();
    double semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    double eccentricity = orbit.getDataFor(ECCENTRICITY);
    double omegaSat = (2 * Math.PI) / (orbit.getDataFor(ORBITAL_PERIOD));
    double inclination = orbit.getDataFor(INCLINATION);
    double j2 = orbit.getBody().getJ2();

    double equationTopHalf = -3 * radiusBody * radiusBody * j2 * omegaSat * Math.cos(inclination);
    double equationBotHalf = 2 * Math.pow((semiMajorAxis * (1 - (eccentricity * eccentricity))), 2);
    omegaP = equationTopHalf / equationBotHalf;

    // omegaP is in rad/s. Convert to rad/day
    omegaP = omegaP * 3600 * 24;
    return omegaP;
  }

  private double precessionFitMethod(Orbit orbit, double desiredPrecession) {
    double omegaP = desiredPrecession;
    double radiusBody = orbit.getBody().getRadius();
    double semiMajorAxis = orbit.getDataFor(SEMI_MAJOR_AXIS);
    double eccentricity = orbit.getDataFor(ECCENTRICITY);
    double omegaSat = (2 * Math.PI) / (orbit.getDataFor(ORBITAL_PERIOD));
    double inclination;
    double j2 = orbit.getBody().getJ2();

    // omegaP is in rad/s. Convert to rad/day
    omegaP = omegaP / (3600 * 24);

    double equationBotHalf = -3 * radiusBody * radiusBody * j2 * omegaSat;
    double equationTopHalf =
        2 * omegaP * Math.pow((semiMajorAxis * (1 - (eccentricity * eccentricity))), 2);
    inclination = equationTopHalf / equationBotHalf;

    return Math.acos(inclination);
  }
}
