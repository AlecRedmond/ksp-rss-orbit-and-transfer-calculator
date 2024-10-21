package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
@NoArgsConstructor
public class Burn {
  private double deltaV;
  private double deltaVMagnitude;
  private KeplerEnums changeApsisEnum;
  private KeplerEnums burnApsisEnum;
  private Orbit initialOrbit;
  private Orbit newOrbit;

  public Burn(Orbit initialOrbit, double newAlt) {
    this.initialOrbit = initialOrbit;

    selectFirstBurnChangeApsis(newAlt);
    double burnAlt = this.initialOrbit.getDataFor(burnApsisEnum);

    OrbitBuilder orbitBuilder = new OrbitBuilder(burnAlt, newAlt);
    newOrbit = orbitBuilder.getOrbit();

    calculateDeltaV(burnApsisEnum,burnApsisEnum);
  }

  public Burn(Orbit transferOrbit, Orbit finalOrbit){
    initialOrbit = transferOrbit;
    newOrbit = finalOrbit;

    findCommonApsis();

  }

  private void findCommonApsis() {
    boolean iPEIsFinalPE = withinMOE(initialOrbit.getDataFor(PERIAPSIS),newOrbit.getDataFor(PERIAPSIS));
    boolean iAPIsFinalPE = withinMOE(initialOrbit.getDataFor(APOAPSIS),newOrbit.getDataFor(PERIAPSIS));
    boolean iPEIsFinalAP = withinMOE(initialOrbit.getDataFor(PERIAPSIS),newOrbit.getDataFor(APOAPSIS));
    boolean iAPIsFinalAP = withinMOE(initialOrbit.getDataFor(APOAPSIS),newOrbit.getDataFor(APOAPSIS));

    try{
    if(iPEIsFinalPE){
      calculateDeltaV(PERIAPSIS,PERIAPSIS);
    } else if(iAPIsFinalPE){
      calculateDeltaV(APOAPSIS,PERIAPSIS);
    } else if(iPEIsFinalAP){
      calculateDeltaV(PERIAPSIS,APOAPSIS);
    } else if(iAPIsFinalAP){
      calculateDeltaV(APOAPSIS,APOAPSIS);
    } else {
      throw new Exception();
    }
    } catch (Exception e){
      System.out.println("Error calculating second burn");
    }
  }

  private boolean withinMOE(double dataFor, double dataFor1) {
      return Math.abs(dataFor - dataFor1) / dataFor < 0.001;
  }

  private void calculateDeltaV(KeplerEnums burnApsisInitial, KeplerEnums burnApsisFinal) {
    KeplerEnums burnVelocityEnumInitial = getVelocityEnumFromApsis(burnApsisInitial);
    KeplerEnums burnVelocityEnumFinal = getVelocityEnumFromApsis(burnApsisFinal);
    double vInitial = initialOrbit.getDataFor(burnVelocityEnumInitial);
    double vFinal = newOrbit.getDataFor(burnVelocityEnumFinal);
    deltaV = vFinal - vInitial;
    deltaVMagnitude = Math.abs(deltaV);
  }

  private KeplerEnums getVelocityEnumFromApsis(KeplerEnums burnApsisEnum) {
    if(burnApsisEnum.equals(PERIAPSIS)){
      return VELOCITY_PERIAPSIS;
    } else {
      return VELOCITY_APOAPSIS;
    }
  }

  private void selectFirstBurnChangeApsis(double newAlt) {
    // Select the one closest to newAlt
    double initialAP = initialOrbit.getDataFor(APOAPSIS);
    double initialPE = initialOrbit.getDataFor(PERIAPSIS);
    if(withinMOE(initialPE,initialAP)){
      if(initialPE > newAlt){
        changeApsisEnum = PERIAPSIS;
        burnApsisEnum = APOAPSIS;
        return;
      }
      else {
        changeApsisEnum = APOAPSIS;
        burnApsisEnum = PERIAPSIS;
        return;
      }
    }

    if (Math.abs(initialAP - newAlt) > Math.abs(initialPE - newAlt)) {
      changeApsisEnum = PERIAPSIS;
      burnApsisEnum = APOAPSIS;
    } else {
      changeApsisEnum = APOAPSIS;
      burnApsisEnum = PERIAPSIS;
    }
  }
}
