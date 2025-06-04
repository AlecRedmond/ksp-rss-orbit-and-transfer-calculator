package org.artools.orbitcalculator.method.writeableorbit;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.artools.orbitcalculator.application.writeableorbit.keplerianelements.Kepler.KeplerEnums;

@Data
@AllArgsConstructor
public class OrbitalParameterHolds {
  private Map<KeplerEnums, Boolean> holdsMap = new HashMap<>();
  private List<KeplerEnums> toggleHistory = new ArrayList<>();

  //Default constructor initializing all orbital parameters to false.
  public OrbitalParameterHolds() {
    initialiseHoldsMap();
  }

  //Constructs an instance with specific parameters enabled.
  public OrbitalParameterHolds(KeplerEnums keplerEnum1, KeplerEnums keplerEnum2) {
    initialiseHoldsMap();
    setHold(keplerEnum1, true);
    setHold(keplerEnum2, true);
  }

  private void initialiseHoldsMap() {
    for(KeplerEnums parameter : KeplerEnums.values()){
      holdsMap.put(parameter,false);
    }
  }

  public void setHold(KeplerEnums keplerEnum, boolean holdState) {
    holdsMap.put(keplerEnum, holdState);
  }

  public boolean getHold(KeplerEnums keplerEnum) {
    return holdsMap.getOrDefault(keplerEnum, false);
  }

  public void toggleParameter(KeplerEnums keplerEnums) {
    boolean wasEnabled;
    if (toggleHistory.contains(keplerEnums)) {
      disableParameter(keplerEnums);
      wasEnabled = false;
    } else {
      enableParameter(keplerEnums);
      wasEnabled = true;
    }
    evaluateToggleRules(keplerEnums, wasEnabled);
  }

  private void enableParameter(KeplerEnums keplerEnum) {
    setHold(keplerEnum, true);
    toggleHistory.add(keplerEnum);
  }

  private void disableParameter(KeplerEnums keplerEnums) {
    setHold(keplerEnums, false);
    toggleHistory.remove(keplerEnums);
  }

  private void evaluateToggleRules(KeplerEnums toggledEnum, boolean wasEnabled) {
    enforceMutualExclusion(KeplerEnums.ORBITAL_PERIOD, KeplerEnums.SEMI_MAJOR_AXIS);
    enforceMutualExclusion(KeplerEnums.NODAL_PRECESSION, KeplerEnums.INCLINATION);

    if (toggleHistory.size() > 2 && isThirdToggleNonCritical()) {
      releaseOldestNonCriticalToggle();
    }

    enforceVelocityConsistency(toggledEnum, wasEnabled);
  }

  private void enforceMutualExclusion(KeplerEnums enumA, KeplerEnums enumB) {
    if (getHold(enumA) && getHold(enumB)) {
      releaseOlderToggle(enumA, enumB);
    }
  }

  private boolean isThirdToggleNonCritical() {
    return !(toggleHistory.contains(KeplerEnums.NODAL_PRECESSION) || toggleHistory.contains(KeplerEnums.INCLINATION));
  }

  private void releaseOlderToggle(KeplerEnums enumA, KeplerEnums enumB) {
    for (KeplerEnums parameter : toggleHistory) {
      if (parameter == enumA || parameter == enumB) {
        disableParameter(parameter);
        break;
      }
    }
  }

  private void releaseOldestNonCriticalToggle() {
    for (KeplerEnums parameter : toggleHistory) {
      if (parameter != KeplerEnums.NODAL_PRECESSION && parameter != KeplerEnums.INCLINATION) {
        disableParameter(parameter);
        return;
      }
    }
  }

  private void enforceVelocityConsistency(KeplerEnums toggledEnum, boolean wasEnabled) {
    if (wasEnabled && !holdsMap.getOrDefault(KeplerEnums.SEMI_MAJOR_AXIS, false) &&
            !holdsMap.getOrDefault(KeplerEnums.ORBITAL_PERIOD, false)) {

      if (toggledEnum == KeplerEnums.VELOCITY_PERIAPSIS) {
        toggleParameter(KeplerEnums.PERIAPSIS);
      } else if (toggledEnum == KeplerEnums.VELOCITY_APOAPSIS) {
        toggleParameter(KeplerEnums.APOAPSIS);
      }
    }
  }


}
