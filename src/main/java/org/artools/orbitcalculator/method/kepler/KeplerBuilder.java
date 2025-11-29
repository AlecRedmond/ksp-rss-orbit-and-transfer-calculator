package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.OrbitalState;

public class KeplerBuilder {
  @Getter private final KeplerOrbit orbit;
  private final Planet centralBody;
  private final KeplerUtils utils;
  private KeplerHolds holds;
  @Getter private boolean built;

  public KeplerBuilder(OrbitalState state) {
    this.centralBody = state.getCentralBody();
    Timestamp timestamp = Timestamp.from(state.getEpoch());
    this.orbit = new KeplerOrbit(timestamp, centralBody.getBodyType());
    this.utils = new KeplerUtils();
    buildOrbitFromState(state);
    built = true;
  }

  private void buildOrbitFromState(OrbitalState state) {
    orbit.setData(ECCENTRICITY, state.getEccentricity().getNorm());
    orbit.setData(SEMI_MAJOR_AXIS, state.getSemiMajorAxis());

    utils.calculateOrbitalPeriod(orbit, centralBody);
    utils.calculateApoapsis(orbit, centralBody);
    utils.calculatePeriapsis(orbit, centralBody);

    orbit.setData(INCLINATION, state.getInclination());
    orbit.setData(LONGITUDE_ASCENDING_NODE, state.getLongitudeAscendingNode());
    orbit.setData(ARGUMENT_OF_PERIAPSIS, state.getArgumentPE());

    orbit.setData(MEAN_ANOMALY, state.getMeanAnomaly());
    orbit.setData(ECCENTRIC_ANOMALY, state.getEccentricAnomaly());
    orbit.setData(TRUE_ANOMALY, state.getTrueAnomaly());

    utils.calculateTimeToPeriapsis(orbit, centralBody);
  }

  public KeplerBuilder(Instant epoch, Planet centralBody) {
    this.centralBody = centralBody;
    this.orbit = new KeplerOrbit(Timestamp.from(epoch), centralBody.getBodyType());
    this.utils = new KeplerUtils();
    this.holds = new KeplerHolds();
    this.built = false;
  }

  public void setData(KeplerElement element, double data) {
    List<KeplerElement> inputElements = holds.addHold(element);
    clearNonHeldElements(inputElements);
    orbit.setData(element, data);
    fillElements(inputElements);
    ensureNotNegative();
  }

  private void clearNonHeldElements(List<KeplerElement> inputElements) {
    orbit.getElementsMap().keySet().stream()
        .filter(element -> !inputElements.contains(element))
        .forEach(orbit::removeEntry);
  }

  private void fillElements(List<KeplerElement> inputElements) {
    if (!holds.isSolvable()) return;
    fillValues(inputElements, ELLIPTICAL_ELEMENTS);
    fillValues(inputElements, EPOCH_ELEMENTS);
    built = true;
  }

  private void ensureNotNegative() {
    orbit.getElementsMap().entrySet().stream()
        .filter(entry -> entry.getValue() < 0)
        .forEach(entry -> utils.convertRadialValuesToPositive(entry.getKey(), orbit));
  }

  private void fillValues(List<KeplerElement> inputElements, Set<KeplerElement> elementsSet) {
    elementsSet.stream()
        .filter(element -> !inputElements.contains(element))
        .forEach(element -> utils.calculateElement(orbit, element, centralBody));
  }
}
