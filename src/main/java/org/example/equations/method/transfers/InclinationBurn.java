package org.example.equations.method.transfers;

import lombok.Data;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
public class InclinationBurn {
  private double inclination;
  private double inclinationDegrees;
  private HohmannTransfer hohmannTransfer;
  private double[] vectorDV;
  private double magnitudeDV;
  private boolean isFirstBurn;
  private double[] otherBurnDV;
  private double magnitudeOtherBurnDV;

  public InclinationBurn(HohmannTransfer hohmannTransfer, double inclinationDegrees) {
    this.hohmannTransfer = hohmannTransfer;
    doInclinationChange(inclinationDegrees);
  }

  private void doInclinationChange(double inclinationDegrees) {
    this.inclinationDegrees = inclinationDegrees;
    inclination = Math.toRadians(inclinationDegrees);

    TangentialBurn altitudeBurn = findHighestAltitudeBurn();

    double initialVelocity =
        altitudeBurn.getInitialOrbit().getDataFor(altitudeBurn.getInitialVelocityEnum());
    double[] initialVelocityVector = vectorise(initialVelocity, 0.0);
    double newVelocity = altitudeBurn.getNewOrbit().getDataFor(altitudeBurn.getNewVelocityEnum());
    double[] newVelocityVector = vectorise(newVelocity, inclination);

    vectorDV = vectorDifference(newVelocityVector, initialVelocityVector);
    magnitudeDV = magnitudeFinder(vectorDV);

    fillOtherBurns();
  }

  private void fillOtherBurns() {
    TangentialBurn otherBurn;
    if(isFirstBurn){
       otherBurn = hohmannTransfer.getSecondBurn();
    } else {
      otherBurn = hohmannTransfer.getFirstBurn();
    }
    otherBurnDV = vectorise(otherBurn.getDeltaV(),0);
    magnitudeOtherBurnDV = magnitudeFinder(otherBurnDV);

  }

  private double magnitudeFinder(double[] vectorDV) {
    double squaredValues = 0;
    for (double element : vectorDV) {
      squaredValues += (element * element);
    }
    return Math.sqrt(squaredValues);
  }

  private double[] vectorDifference(double[] newVelocityVector, double[] initialVelocityVector) {
    for (int i = 0; i < newVelocityVector.length; i++) {
      newVelocityVector[i] = newVelocityVector[i] - initialVelocityVector[i];
    }
    return newVelocityVector;
  }

  private double[] vectorise(double initialVelocity, double rads) {
    double[] vectorisedV = new double[2];
    vectorisedV[0] = Math.cos(rads) * initialVelocity;
    vectorisedV[1] = Math.sin(rads) * initialVelocity;
    return vectorisedV;
  }

  private TangentialBurn findHighestAltitudeBurn() {
    TangentialBurn firstBurn = hohmannTransfer.getFirstBurn();
    TangentialBurn secondBurn = hohmannTransfer.getSecondBurn();

    KeplerEnums firstBurnLocation = firstBurn.getBurnApsisEnum();
    KeplerEnums secondBurnLocation = secondBurn.getBurnApsisEnum();

    double firstAltitude = firstBurn.getInitialOrbit().getDataFor(firstBurnLocation);
    double secondAltitude = secondBurn.getInitialOrbit().getDataFor(secondBurnLocation);

    isFirstBurn = (firstAltitude > secondAltitude);
    return (firstAltitude > secondAltitude) ? firstBurn : secondBurn;
  }
}
