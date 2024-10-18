package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import lombok.Data;
import org.example.equations.application.Keplerian;
import org.example.equations.application.KeplerianHolds;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
public class HohmannTransfer {
  private Keplerian initialOrbit;
  private double firstBurn;
  private KeplerEnums apsisOfFirstBurn;
  private Keplerian transferOrbit;
  private double secondBurn;
  private Keplerian finalOrbit;
  private double totalBurnDV;
  private KeplerEnums apsisOfSecondBurn;

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
    double transfer1TotalDeltaV = totaldVExpended(transfer1deltaVs);

    //Type 2 = first burn at apoapsis
    potentialTransfer2 = fillKeplerian(potentialTransfer2,finalPeriapsis,initialApoapsis);
    double[] transfer2deltaVs = getDeltaVArray(potentialTransfer2,false);
    double transfer2TotalDeltaV = totaldVExpended(transfer2deltaVs);
    
    boolean type1MoreEfficient = compareMagnitudes(transfer1TotalDeltaV,transfer2TotalDeltaV);
    if(type1MoreEfficient){
      transferOrbit = potentialTransfer1;
      firstBurn = transfer1deltaVs[0];
      secondBurn = transfer1deltaVs[1];
      totalBurnDV = transfer1TotalDeltaV;
      apsisOfFirstBurn = PERIAPSIS;
      apsisOfSecondBurn = APOAPSIS;
    } else {
      transferOrbit = potentialTransfer2;
      firstBurn = transfer2deltaVs[0];
      secondBurn = transfer2deltaVs[1];
      totalBurnDV = transfer2TotalDeltaV;
      apsisOfFirstBurn = APOAPSIS;
      apsisOfSecondBurn = PERIAPSIS;
    }

  }

  private boolean compareMagnitudes(double totalDeltaV1, double totalDeltaV2) {
    if(totalDeltaV1 <= totalDeltaV2){
      return true;
    }
    return false;
  }

  private double totaldVExpended(double[] transferDeltaVs) {
    double totaldV = 0;
    for(double burn : transferDeltaVs){
      totaldV += Math.abs(burn);
    }
    return totaldV;
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
