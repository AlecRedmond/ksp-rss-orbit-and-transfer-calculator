package org.artools.orbitcalculator.equations.method.transfers;

import lombok.Data;
import org.artools.orbitcalculator.equations.application.Orbit;
import org.artools.orbitcalculator.equations.application.keplerianelements.Kepler;

import static org.artools.orbitcalculator.equations.application.keplerianelements.Kepler.KeplerEnums.*;

@Data
public class HohmannTransfer {
  private Orbit initialOrbit;
  private TangentialBurn firstBurn;
  private double firstBurnDV;
  private Kepler.KeplerEnums apsisOfFirstBurn;
  private Orbit transferOrbit;
  private TangentialBurn secondBurn;
  private double secondBurnDV;
  private Orbit finalOrbit;
  private double totalBurnDV;
  private Kepler.KeplerEnums apsisOfSecondBurn;

  public HohmannTransfer(Orbit initialOrbit, Orbit finalOrbit) {
    this.initialOrbit = initialOrbit;
    this.finalOrbit = finalOrbit;
    findEfficientTransfers();
  }

  private void findEfficientTransfers() {
    double finalPE = finalOrbit.getDataFor(PERIAPSIS);
    double finalAP = finalOrbit.getDataFor(APOAPSIS);

    TangentialBurn tangentialBurnToTransfer1 = new TangentialBurn(initialOrbit, finalPE);
    TangentialBurn tangentialBurnToTransfer2 = new TangentialBurn(initialOrbit, finalAP);

    Orbit transfer1 = tangentialBurnToTransfer1.getNewOrbit();
    Orbit transfer2 = tangentialBurnToTransfer2.getNewOrbit();

    TangentialBurn tangentialBurnToFinal1 = new TangentialBurn(transfer1, finalOrbit);
    TangentialBurn tangentialBurnToFinal2 = new TangentialBurn(transfer2, finalOrbit);

    double burn1Mags =
        tangentialBurnToTransfer1.getDeltaVMagnitude()
            + tangentialBurnToFinal1.getDeltaVMagnitude();
    double burn2Mags =
        tangentialBurnToTransfer2.getDeltaVMagnitude()
            + tangentialBurnToFinal2.getDeltaVMagnitude();

    if (burn2Mags < burn1Mags) {
      firstBurn = tangentialBurnToTransfer2;
      firstBurnDV = tangentialBurnToTransfer2.getDeltaV();
      apsisOfFirstBurn = tangentialBurnToTransfer2.getBurnApsisEnum();
      transferOrbit = transfer2;

      secondBurn = tangentialBurnToFinal2;
      secondBurnDV = tangentialBurnToFinal2.getDeltaV();
      apsisOfSecondBurn = tangentialBurnToFinal2.getBurnApsisEnum();
      totalBurnDV = burn2Mags;
    } else {
      firstBurn = tangentialBurnToTransfer1;
      firstBurnDV = tangentialBurnToTransfer1.getDeltaV();
      apsisOfFirstBurn = tangentialBurnToTransfer1.getBurnApsisEnum();
      transferOrbit = transfer1;

      secondBurn = tangentialBurnToFinal1;
      secondBurnDV = tangentialBurnToFinal1.getDeltaV();
      apsisOfSecondBurn = tangentialBurnToFinal1.getBurnApsisEnum();
      totalBurnDV = burn1Mags;
    }
  }
}
