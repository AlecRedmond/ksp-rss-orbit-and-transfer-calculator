package org.example.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.equations.application.Orbit;
import org.example.equations.application.OrbitalParameterHolds;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.method.HohmannTransfer;
import org.example.equations.method.OrbitBuilder;

import java.util.HashMap;

@Data
public class WorkingLogic {
    @Getter
    @Setter
    private static boolean doWork = true;
    @Getter
    @Setter
    private static OrbitBuilder[] orbitBuilders;
    @Getter
    @Setter
    private static HohmannTransfer hohmannTransfer;
    @Getter
    @Setter
    private static Orbit[] orbits;

    public static void doVisVivaTransfer() {
        orbits = new Orbit[]{orbitBuilders[0].getOrbit(),orbitBuilders[1].getOrbit()};
        hohmannTransfer = new HohmannTransfer(orbits[0],orbits[1]);

    }

}
