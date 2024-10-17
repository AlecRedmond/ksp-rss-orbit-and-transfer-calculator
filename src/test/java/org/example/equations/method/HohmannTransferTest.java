package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.equations.application.Keplerian;
import org.example.equations.application.KeplerianHolds;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HohmannTransferTest {
    private static KeplerianHolds keplerianHolds;
    private static Keplerian initialOrbit;
    private static Keplerian finalOrbit;
    private static double initialPE = 300000;
    private static double initialAP = 500000;
    private static double finalPE = 250000;
    private static double finalAP = 600000;

    @BeforeAll
    public static void initialiseKeplerians(){
        keplerianHolds = new KeplerianHolds();
        keplerianHolds.setHold(PERIAPSIS,true);
        keplerianHolds.setHold(APOAPSIS,true);

        initialOrbit = new Keplerian();
        initialOrbit.setDataFor(PERIAPSIS,initialPE);
        initialOrbit.setDataFor(APOAPSIS,initialAP);

        finalOrbit = new Keplerian();
        finalOrbit.setDataFor(PERIAPSIS,finalPE);
        finalOrbit.setDataFor(APOAPSIS,finalAP);

        KeplerianMethod keplerianMethod = new KeplerianMethod(initialOrbit,keplerianHolds);
        initialOrbit = keplerianMethod.getKeplerian();
        keplerianMethod = new KeplerianMethod(finalOrbit,keplerianHolds);
        finalOrbit = keplerianMethod.getKeplerian();
    }

    @Test
    public void testHohmannTransfer(){
        HohmannTransfer hohmannTransfer = new HohmannTransfer(initialOrbit,finalOrbit);
        System.out.println(hohmannTransfer.getTransferOrbit());
        System.out.println(hohmannTransfer.getFirstBurn());
        System.out.println(hohmannTransfer.getSecondBurn());
    }

}
