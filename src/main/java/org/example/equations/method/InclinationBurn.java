package org.example.equations.method;

import lombok.Data;
import org.example.equations.application.Orbit;
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

  public InclinationBurn(Orbit firstOrbit, Orbit finalOrbit, double inclinationDegrees) {
    hohmannTransfer = new HohmannTransfer(firstOrbit, finalOrbit);
    doInclinationChange(inclinationDegrees);
  }

  private void doInclinationChange(double inclinationDegrees) {
    this.inclinationDegrees = inclinationDegrees;
    inclination = Math.toRadians(inclinationDegrees);

    TangentialBurn altitudeBurn = findHighestAltitudeBurn();

    double initialVelocity =
        altitudeBurn.getInitialOrbit().getDataFor(altitudeBurn.getInitialVelocityEnum());
    double[] initialVelocityVect = vectorise(initialVelocity, 0.0);
    double newVelocity = altitudeBurn.getNewOrbit().getDataFor(altitudeBurn.getNewVelocityEnum());
    double[] newVelocityVect = vectorise(newVelocity, inclination);

    vectorDV = vectorDifference(newVelocityVect, initialVelocityVect);
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

  private double[] vectorDifference(double[] newVelocityVect, double[] initialVelocityVect) {
    for (int i = 0; i < newVelocityVect.length; i++) {
      newVelocityVect[i] = newVelocityVect[i] - initialVelocityVect[i];
    }
    return newVelocityVect;
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
