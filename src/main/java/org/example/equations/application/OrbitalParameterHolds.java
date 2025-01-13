package org.example.equations.application;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
@AllArgsConstructor
public class OrbitalParameterHolds {
  private Map<KeplerEnums, Boolean> holdsMap = new HashMap<>();
  private List<KeplerEnums> orderedClickList = new ArrayList<>();

  public OrbitalParameterHolds() {
    buildHolds();
  }

  public OrbitalParameterHolds(KeplerEnums keplerEnum1, KeplerEnums keplerEnum2) {
    buildHolds();
    setHold(keplerEnum1, true);
    setHold(keplerEnum2, true);
  }

  private void buildHolds() {
    holdsMap.putAll(
        Map.of(
            APOAPSIS, false,
            PERIAPSIS, false,
            ECCENTRICITY, false,
            SEMI_MAJOR_AXIS, false,
            ORBITAL_PERIOD, false,
            VELOCITY_APOAPSIS, false,
            VELOCITY_PERIAPSIS, false));
  }

  public void setHold(KeplerEnums keplerEnum, boolean holdState) {
    holdsMap.put(keplerEnum, holdState);
  }

  public boolean getHold(KeplerEnums keplerEnum) {
    return holdsMap.getOrDefault(keplerEnum, false);
  }

  // Toggle Button Logic

  public void toggleButtonClicked(KeplerEnums keplerEnums) {
    boolean wasEnabled;
    if (orderedClickList.contains(keplerEnums)) {
      releaseToggle(keplerEnums);
      wasEnabled = false;
    } else {
      setHold(keplerEnums, true);
      orderedClickList.add(keplerEnums);
      wasEnabled = true;
    }
    toggleButtonMethod(keplerEnums, wasEnabled);
  }

  private void toggleButtonMethod(KeplerEnums keplerEnums, boolean wasEnabled) {
    Optional<KeplerEnums> lastPopped = Optional.empty();

    if (getHold(ORBITAL_PERIOD) && getHold(SEMI_MAJOR_AXIS)) {
      releaseOlderOfTwoToggle(ORBITAL_PERIOD, SEMI_MAJOR_AXIS);
    }
    if (getHold(NODAL_PRECESSION) && getHold(INCLINATION)) {
      releaseOlderOfTwoToggle(NODAL_PRECESSION, INCLINATION);
    }
    if (orderedClickList.size() > 2 && thirdToggleNotInclinationOrNodalPrecession()) {
      lastPopped = releaseOldestNonInclinedToggle();
    }

    velocityToggleComparator(keplerEnums, VELOCITY_PERIAPSIS, PERIAPSIS, wasEnabled, lastPopped);
    velocityToggleComparator(keplerEnums, VELOCITY_APOAPSIS, APOAPSIS, wasEnabled, lastPopped);
  }

  private boolean thirdToggleNotInclinationOrNodalPrecession() {
    return !(orderedClickList.size() == 3
        && (orderedClickList.contains(NODAL_PRECESSION) || orderedClickList.contains(INCLINATION)));
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private void velocityToggleComparator(
      KeplerEnums keplerEnums,
      KeplerEnums velocity,
      KeplerEnums apsis,
      boolean wasEnabled,
      Optional<KeplerEnums> lastPopped) {
    if (wasEnabled && (!getHold(SEMI_MAJOR_AXIS) && !getHold(ORBITAL_PERIOD))) {
      if (keplerEnums.equals(velocity)) {
        if (lastPopped.isPresent()
            && (lastPopped.get().equals(SEMI_MAJOR_AXIS)
                || lastPopped.get().equals(ORBITAL_PERIOD))) {
          toggleButtonClicked(lastPopped.get());
        } else {
          toggleButtonClicked(apsis);
        }
      } else if (getHold(velocity) != getHold(apsis)) {
        releaseToggle(velocity);
      }
    }
  }

  private Optional<KeplerEnums> releaseOldestNonInclinedToggle() {
    Optional<KeplerEnums> valueToPop =
        orderedClickList.stream()
            .filter(e -> (!e.equals(NODAL_PRECESSION) && !e.equals(INCLINATION)))
            .findFirst();

    if (valueToPop.isPresent()){
      releaseToggle(valueToPop.get());
      return valueToPop;
    } else {
      return Optional.empty();
    }
  }

  private void releaseOlderOfTwoToggle(KeplerEnums enumA, KeplerEnums enumB) {
    Optional<KeplerEnums> first =
        orderedClickList.stream().filter(e -> e.equals(enumA) || e.equals(enumB)).findFirst();
    first.ifPresent(this::releaseToggle);
  }

  private void releaseToggle(KeplerEnums keplerEnums) {
    orderedClickList.remove(keplerEnums);
    setHold(keplerEnums, false);
  }
}
