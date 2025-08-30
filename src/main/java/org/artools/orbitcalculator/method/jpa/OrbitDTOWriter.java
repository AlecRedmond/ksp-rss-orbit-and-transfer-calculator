package org.artools.orbitcalculator.method.jpa;

import java.sql.Timestamp;
import lombok.Getter;
import org.artools.orbitcalculator.application.jpa.OrbitDTO;
import org.artools.orbitcalculator.application.vector.OrbitalState;

@Getter
public class OrbitDTOWriter {
  private final OrbitDTO orbitDTO;

  public OrbitDTOWriter(OrbitalState state) {
    this.orbitDTO = writeOrbit(state);
  }

  private OrbitDTO writeOrbit(OrbitalState state) {
    double eccentricity = state.getEccentricity().getNorm();
    double bodyRadius = state.getCentralBody().getBodyRadius();
    double sma = state.getSemiMajorAxis();
    double apoapsisAlt = sma * (1 + eccentricity) - bodyRadius;
    double periapsisAlt = sma * (1 - eccentricity) - bodyRadius;
    double altitude = state.getPosition().getNorm() - bodyRadius;
    double velocity = state.getVelocity().getNorm();

    return OrbitDTO.builder()
        .orbitFocus(state.getCentralBody().getBodyType().toString())
        .apoapsisAlt(apoapsisAlt)
        .periapsisAlt(periapsisAlt)
        .eccentricity(eccentricity)
        .semiMajorAxis(sma)
        .rightAscension(state.getLongitudeAscendingNode())
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
