package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.jpa.OrbitStateDTO;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.application.vector.Orrery;

public class KeplerBuilder {
  @Getter private final KeplerOrbit orbit;
  private final Orrery orrery;
  private final KeplerUtils utils;
  private KeplerHolds holds;
  @Getter private boolean built;

  public KeplerBuilder(OrbitStateDTO osDTO, Orrery orrery) {
    this.orbit = new KeplerOrbit(osDTO.getTimestamp(), osDTO.getCentralBodyType());
    this.orrery = orrery;
    this.utils = new KeplerUtils();
    buildOrbitFromState(osDTO);
    built = true;
  }

  private void buildOrbitFromState(OrbitStateDTO osDTO) {
    orbit.setData(ECCENTRICITY, osDTO.getEccentricity());
    orbit.setData(SEMI_MAJOR_AXIS, osDTO.getSemiMajorAxis());

    utils.calculateOrbitalPeriod(orbit, orrery);
    utils.calculateApoapsis(orbit, orrery);
    utils.calculatePeriapsis(orbit, orrery);

    orbit.setData(INCLINATION, osDTO.getInclination());
    orbit.setData(LONGITUDE_ASCENDING_NODE, osDTO.getLongitudeAscendingNode());
    orbit.setData(ARGUMENT_OF_PERIAPSIS, osDTO.getArgumentPE());

    orbit.setData(MEAN_ANOMALY, osDTO.getMeanAnomaly());
    orbit.setData(ECCENTRIC_ANOMALY, osDTO.getEccentricAnomaly());
    orbit.setData(TRUE_ANOMALY, osDTO.getTrueAnomaly());

    utils.calculateTimeToPeriapsis(orbit, orrery);
  }

  public KeplerBuilder(Instant epoch, BodyType centralBodyType, Orrery orrery) {
    this.orrery = orrery;
    this.orbit = new KeplerOrbit(Timestamp.from(epoch), centralBodyType);
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
        .forEach(element -> utils.calculateElement(orbit, element, orrery));
  }
}
