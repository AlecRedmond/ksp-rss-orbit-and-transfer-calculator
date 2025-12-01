package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;
import static org.artools.orbitcalculator.method.kepler.KeplerUtils.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.artools.orbitcalculator.application.kepler.KeplerHolds;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.OrbitalState;

@Getter
public class KeplerBuilder {
  private final KeplerOrbit orbit;

  public KeplerBuilder(OrbitalState state) {
    Planet centralBody = state.getCentralBody();
    Timestamp timestamp = Timestamp.from(state.getEpoch());
    this.orbit = new KeplerOrbit(timestamp, centralBody.getBodyType());
    buildOrbitFromState(state, centralBody);
    ensureAllWithinBounds(ROTATIONAL_ELEMENTS);
    ensureAllWithinBounds(POSITION_ELEMENTS);
  }

  private void buildOrbitFromState(OrbitalState state, Planet centralBody) {
    orbit.setData(ECCENTRICITY, state.getEccentricity().getNorm());
    orbit.setData(SEMI_MAJOR_AXIS, state.getSemiMajorAxis());

    KeplerUtils.calculateOrbitalPeriod(orbit, centralBody);
    KeplerUtils.calculateApoapsis(orbit, centralBody);
    KeplerUtils.calculatePeriapsis(orbit, centralBody);

    orbit.setData(INCLINATION, state.getInclination());
    orbit.setData(LONGITUDE_ASCENDING_NODE, state.getLongitudeAscendingNode());
    orbit.setData(ARGUMENT_OF_PERIAPSIS, state.getArgumentPE());

    orbit.setData(MEAN_ANOMALY, state.getMeanAnomaly());
    orbit.setData(ECCENTRIC_ANOMALY, state.getEccentricAnomaly());
    orbit.setData(TRUE_ANOMALY, state.getTrueAnomaly());

    KeplerUtils.calculateTimeToPeriapsis(orbit, centralBody);

    orbit.setAllElementsBuilt(true);
  }

  private void ensureAllWithinBounds(Set<KeplerElement> elementType) {
    orbit.getElementsMap().keySet().stream()
        .filter(elementType::contains)
        .forEach(element -> KeplerUtils.ensureValuesWithinBounds(element, orbit));
  }

  public KeplerBuilder(KeplerOrbit orbit, Planet centralBody, KeplerHolds holds) {
    this.orbit = orbit;
    checkOrbitContainsHolds(orbit, holds);
    Set<KeplerElement> unHeldElements = getUnHeldElements(holds);
    trimOrbitToHolds(orbit, unHeldElements);
    fillSolvableElements(orbit, centralBody, holds, unHeldElements);
  }

  private void checkOrbitContainsHolds(KeplerOrbit orbit, KeplerHolds holds) {
    boolean holdsMatch =
        holds.getHeldElements().stream().allMatch(orbit.getElementsMap()::containsKey);
    if (holdsMatch) return;
    throw new IllegalArgumentException("Orbit did not contain data for all held elements!");
  }

  private Set<KeplerElement> getUnHeldElements(KeplerHolds holds) {
    return Arrays.stream(values())
        .filter(keplerElement -> !holds.getHeldElements().contains(keplerElement))
        .collect(Collectors.toSet());
  }

  private void trimOrbitToHolds(KeplerOrbit orbit, Set<KeplerElement> unHeldElements) {
    unHeldElements.forEach(orbit.getElementsMap()::remove);
  }

  private void fillSolvableElements(
      KeplerOrbit orbit, Planet centralBody, KeplerHolds holds, Set<KeplerElement> unHeldElements) {
    if (holds.isEllipticSolvable()) {
      fillValues(orbit, unHeldElements, ELLIPTICAL_ELEMENTS, centralBody);
    }
    if (holds.isRotationalSolvable()) {
      fillValues(orbit, unHeldElements, ROTATIONAL_ELEMENTS, centralBody);
      ensureAllWithinBounds(ROTATIONAL_ELEMENTS);
    }
    if (holds.isPositionSolvable()) {
      fillValues(orbit, unHeldElements, POSITION_ELEMENTS, centralBody);
      ensureAllWithinBounds(POSITION_ELEMENTS);
    }
    if (holds.isAllSolvable()) {
      orbit.setAllElementsBuilt(true);
    }
  }

  private void fillValues(
      KeplerOrbit orbit,
      Set<KeplerElement> unHeldElements,
      Set<KeplerElement> elementType,
      Planet centralBody) {
    unHeldElements.stream()
        .filter(elementType::contains)
        .forEach(keplerElement -> KeplerUtils.calculateElement(orbit, keplerElement, centralBody));
  }
}
