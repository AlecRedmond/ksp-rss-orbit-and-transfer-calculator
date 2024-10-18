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
    private static double initialPE = 300000;
    private static double initialAP = 500000;
    private static double finalPE = 10000000;
    private static double finalAP = 40000000;

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
        System.out.println(hohmannTransfer.getTransferOrbit());
        System.out.println(hohmannTransfer.getFirstBurn());
        System.out.println(hohmannTransfer.getSecondBurn());
        System.out.println(hohmannTransfer.getTotalBurnDV());
        System.out.println(hohmannTransfer.getApsisOfFirstBurn());
    }

}
