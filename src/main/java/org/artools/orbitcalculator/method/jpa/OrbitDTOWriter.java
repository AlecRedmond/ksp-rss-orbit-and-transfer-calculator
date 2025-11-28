package org.artools.orbitcalculator.method.jpa;

import java.sql.Timestamp;
import lombok.Getter;
import org.artools.orbitcalculator.application.jpa.OrbitStateDTO;
import org.artools.orbitcalculator.application.vector.OrbitalState;

@Getter
public class OrbitDTOWriter {

  private OrbitDTOWriter() {}

  public static OrbitStateDTO writeOrbit(OrbitalState state) {
    double eccentricity = state.getEccentricity().getNorm();
    double bodyRadius = state.getCentralBody().getBodyRadius();
    double sma = state.getSemiMajorAxis();
    double apoapsisAlt = sma * (1 + eccentricity) - bodyRadius;
    double periapsisAlt = sma * (1 - eccentricity) - bodyRadius;
    double altitude = state.getPosition().getNorm() - bodyRadius;
    double velocity = state.getVelocity().getNorm();

    return OrbitStateDTO.builder()
        .centralBody(state.getCentralBody().getBodyType())
        .apoapsisAlt(apoapsisAlt)
        .periapsisAlt(periapsisAlt)
        .eccentricity(eccentricity)
        .semiMajorAxis(sma)
        .longitudeAscendingNode(state.getLongitudeAscendingNode())
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
