package org.example.equations.method;

import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.APOAPSIS;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.PERIAPSIS;

@Data
public class HohmannTransfer {
    private Orbit initialOrbit;
    private double firstBurn;
    private Kepler.KeplerEnums apsisOfFirstBurn;
    private Orbit transferOrbit;
    private double secondBurn;
    private Orbit finalOrbit;
    private double totalBurnDV;
    private Kepler.KeplerEnums apsisOfSecondBurn;

    public HohmannTransfer(Orbit initialOrbit,Orbit finalOrbit){
        this.initialOrbit = initialOrbit;
        this.finalOrbit = finalOrbit;
        findEfficientTransfers();
    }

    private void findEfficientTransfers() {
        double finalPE = finalOrbit.getDataFor(PERIAPSIS);
        double finalAP = finalOrbit.getDataFor(APOAPSIS);

        Burn burnToTransfer1 = new Burn(initialOrbit,finalPE);
        Burn burnToTransfer2 = new Burn(initialOrbit,finalAP);

        Orbit transfer1 = burnToTransfer1.getNewOrbit();
        Orbit transfer2 = burnToTransfer2.getNewOrbit();

        Burn burnToFinal1 = new Burn(transfer1,finalOrbit);
        Burn burnToFinal2 = new Burn(transfer2,finalOrbit);

        double burn1Mags = burnToTransfer1.getDeltaVMagnitude() + burnToFinal1.getDeltaVMagnitude();
        double burn2Mags = burnToTransfer2.getDeltaVMagnitude() + burnToFinal2.getDeltaVMagnitude();

        if(burn2Mags < burn1Mags){
            firstBurn = burnToTransfer2.getDeltaV();
            apsisOfFirstBurn = burnToTransfer2.getBurnApsisEnum();
            transferOrbit = transfer2;
            secondBurn = burnToFinal2.getDeltaV();
            apsisOfSecondBurn = oppositeApsis(apsisOfFirstBurn);
            totalBurnDV = burn2Mags;
        } else {
            firstBurn = burnToTransfer1.getDeltaV();
            apsisOfFirstBurn = burnToTransfer1.getBurnApsisEnum();
            transferOrbit = transfer1;
            secondBurn = burnToFinal1.getDeltaV();
            apsisOfSecondBurn = oppositeApsis(apsisOfFirstBurn);
            totalBurnDV = burn1Mags;
        }
    }

    private Kepler.KeplerEnums oppositeApsis(Kepler.KeplerEnums apsisOfFirstBurn) {
        if(apsisOfFirstBurn.equals(PERIAPSIS)){
            return APOAPSIS;
        } else {
            return PERIAPSIS;
        }
    }

}
