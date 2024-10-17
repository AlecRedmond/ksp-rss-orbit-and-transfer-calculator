package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import lombok.Data;
import org.example.equations.application.Keplerian;
import org.example.equations.application.KeplerianHolds;

@Data
public class HohmannTransfer {
  private Keplerian initialOrbit;
  private double firstBurn;
  private Keplerian transferOrbit;
  private double secondBurn;
  private Keplerian finalOrbit;

  public HohmannTransfer(Keplerian initialOrbit, Keplerian finalOrbit) {
    this.initialOrbit = initialOrbit;
    this.finalOrbit = finalOrbit;
    calculateTransferOrbit();
  }

  private void calculateTransferOrbit() {
    Keplerian potentialTransfer1 = new Keplerian();
    Keplerian potentialTransfer2 = new Keplerian();


    double initialApoapsis = initialOrbit.getDataFor(APOAPSIS);
    double initialPeriapsis = initialOrbit.getDataFor(PERIAPSIS);
    double finalApoapsis = finalOrbit.getDataFor(APOAPSIS);
    double finalPeriapsis = finalOrbit.getDataFor(PERIAPSIS);

    //Type 1 = first burn at periapsis
    potentialTransfer1 = fillKeplerian(potentialTransfer1,initialPeriapsis,finalApoapsis);
    double[] transfer1deltaVs = getDeltaVArray(potentialTransfer1,true);

    //Type 2 = first burn at apoapsis
    potentialTransfer2 = fillKeplerian(potentialTransfer2,finalPeriapsis,initialApoapsis);
    double[] transfer2deltaVs = getDeltaVArray(potentialTransfer2,false);
    
    boolean type1MoreEfficient = compareMagnitudes(transfer1deltaVs,transfer2deltaVs);
    if(type1MoreEfficient){
      transferOrbit = potentialTransfer1;
      firstBurn = transfer1deltaVs[0];
      secondBurn = transfer1deltaVs[1];
    } else {
      transferOrbit = potentialTransfer2;
      firstBurn = transfer2deltaVs[0];
      secondBurn = transfer2deltaVs[1];
    }

  }

  private boolean compareMagnitudes(double[] transfer1deltaVs, double[] transfer2deltaVs) {
    if(totalBurnMagnitude(transfer1deltaVs) <= totalBurnMagnitude(transfer2deltaVs)){
      return true;
    }
    return false;
  }

  private double totalBurnMagnitude(double[] transferDeltaVs) {
    double vectorMagnitude = 0;
    for(double vector : transferDeltaVs){
      vectorMagnitude += Math.abs(vector);
    }
    return vectorMagnitude;
  }

  private double[] getDeltaVArray(Keplerian potentialTransfer, boolean isType1) {
    double[] transfers = new double[2];
    if(isType1){
      transfers[0] = potentialTransfer.getDataFor(VELOCITY_PERIAPSIS) - initialOrbit.getDataFor(VELOCITY_PERIAPSIS);
      transfers[1] = finalOrbit.getDataFor(VELOCITY_APOAPSIS) - potentialTransfer.getDataFor(VELOCITY_APOAPSIS);
    } else {
      transfers[0] = potentialTransfer.getDataFor(VELOCITY_APOAPSIS) - initialOrbit.getDataFor(VELOCITY_APOAPSIS);
      transfers[1] = finalOrbit.getDataFor(VELOCITY_PERIAPSIS) - potentialTransfer.getDataFor(VELOCITY_PERIAPSIS);
    }
    
    return transfers;
  }

  private Keplerian fillKeplerian(Keplerian keplerian,double periapsis,double apoapsis){
    KeplerianHolds transferHolds = new KeplerianHolds();
    transferHolds.setHold(PERIAPSIS,true);
    transferHolds.setHold(APOAPSIS,true);

    keplerian.setDataFor(PERIAPSIS,periapsis);
    keplerian.setDataFor(APOAPSIS,apoapsis);
    KeplerianMethod keplerianMethod = new KeplerianMethod(keplerian,transferHolds);
    return keplerianMethod.getKeplerian();
  }
}
