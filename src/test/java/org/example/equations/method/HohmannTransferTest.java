package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import org.example.equations.application.Orbit;
import org.example.equations.application.OrbitalParameterHolds;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HohmannTransferTest {
    private static OrbitalParameterHolds orbitalParameterHolds;
    private static Orbit initialOrbit;
    private static Orbit finalOrbit;
    private static double initialPE = 350000;
    private static double initialAP = 350000;
    private static double finalPE = 50000;
    private static double finalAP = 400000;

    @BeforeAll
    public static void initialiseKeplerians(){
        orbitalParameterHolds = new OrbitalParameterHolds();
        orbitalParameterHolds.setHold(PERIAPSIS,true);
        orbitalParameterHolds.setHold(APOAPSIS,true);

        initialOrbit = new Orbit();
        initialOrbit.setDataFor(PERIAPSIS,initialPE);
        initialOrbit.setDataFor(APOAPSIS,initialAP);

        finalOrbit = new Orbit();
        finalOrbit.setDataFor(PERIAPSIS,finalPE);
        finalOrbit.setDataFor(APOAPSIS,finalAP);

        OrbitBuilder orbitBuilder = new OrbitBuilder(initialOrbit, orbitalParameterHolds);
        initialOrbit = orbitBuilder.getOrbit();
        orbitBuilder = new OrbitBuilder(finalOrbit, orbitalParameterHolds);
        finalOrbit = orbitBuilder.getOrbit();
    }

    @Test
    public void testHohmannTransfer(){
        HohmannTransfer hohmannTransfer = new HohmannTransfer(initialOrbit,finalOrbit);
        Orbit transferOrbit = hohmannTransfer.getTransferOrbit();
        System.out.println("BURN AT: " + hohmannTransfer.getApsisOfFirstBurn());
        System.out.println("PERIAPSIS " + transferOrbit.getDataFor(PERIAPSIS));
        System.out.println("APOAPSIS " + transferOrbit.getDataFor(APOAPSIS));
        System.out.println("FIRST BURN: " + hohmannTransfer.getFirstBurn());
        System.out.println("SECOND BURN: " + hohmannTransfer.getSecondBurn());
        System.out.println("TOTAL DV: " + hohmannTransfer.getTotalBurnDV());
    }

}
