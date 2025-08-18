package org.artools.orbitcalculator.method.vector;

import java.sql.Timestamp;
import lombok.Getter;
import org.artools.orbitcalculator.application.OrbitInfo;
import org.artools.orbitcalculator.application.vector.OrbitalState;

@Getter
public class OrbitInfoWriter {
  private final OrbitInfo orbitInfo;

  public OrbitInfoWriter(OrbitalState state) {
    this.orbitInfo = writeOrbit(state);
  }

  private OrbitInfo writeOrbit(OrbitalState state) {
    double eccentricity = state.getEccentricity().getNorm();
    double bodyRadius = state.getCentralBody().getBodyRadius();
    double sma = state.getSemiMajorAxis();
    double apoapsisAlt = sma * (1 + eccentricity) - bodyRadius;
    double periapsisAlt = sma * (1 - eccentricity) - bodyRadius;
    double altitude = state.getPosition().getNorm() - bodyRadius;
    double velocity = state.getVelocity().getNorm();

    return OrbitInfo.builder()
        .orbitFocus(state.getCentralBody().getBodyType().toString())
        .apoapsisAlt(apoapsisAlt)
        .periapsisAlt(periapsisAlt)
        .eccentricity(eccentricity)
        .semiMajorAxis(sma)
        .rightAscension(state.getRightAscension())
        .inclination(state.getInclination())
        .argumentPE(state.getArgumentPE())
        .trueAnomaly(state.getTrueAnomaly())
        .eccentricAnomaly(state.getEccentricAnomaly())
        .meanAnomaly(state.getMeanAnomaly())
        .altitude(altitude)
        .orbitalVelocity(velocity)
        .timestamp(Timestamp.from(state.getEpoch()))
        .build();
  }
}
