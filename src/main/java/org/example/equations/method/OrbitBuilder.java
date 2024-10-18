package org.example.equations.method;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.equations.application.Orbit;
import org.example.equations.application.OrbitalParameterHolds;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

import java.util.Map;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

@Data
@NoArgsConstructor
public class OrbitBuilder {
    private Orbit orbit;
    private OrbitalParameterHolds orbitalParameterHolds;

    public OrbitBuilder(Orbit orbit, OrbitalParameterHolds orbitalParameterHolds){
        this.orbit = orbit;
        this.orbitalParameterHolds = orbitalParameterHolds;
        methodFromHolds();
    }

    private void methodFromHolds() {
        int holdsPressed = countHolds(orbitalParameterHolds);
        if(holdsPressed != 2){
            setAllToZero();
            return;
        }
        if(held(ORBITAL_PERIOD)){
            FillEquations.convertOrbitalPeriodToSMA(orbit);
            orbitalParameterHolds.setHold(SEMI_MAJOR_AXIS,true);
        }

        if(held(APOAPSIS) || held(PERIAPSIS)){
            if(held(APOAPSIS) && held(PERIAPSIS)){
                FillEquations.calculateFromPeriapsisApoapsis(orbit);
            } else if(held(ECCENTRICITY)){
                FillEquations.calculateFromApsisEccentricity(orbit,held(PERIAPSIS));
            } else if(held(SEMI_MAJOR_AXIS)){
                FillEquations.calculateFromApsisSemiMajorAxis(orbit,held(PERIAPSIS));
            } else if(held(VELOCITY_PERIAPSIS) && held(PERIAPSIS) || held(VELOCITY_APOAPSIS) && held(APOAPSIS)){
                FillEquations.calculateSMAFromVelocityAndAltitude(orbit,held(PERIAPSIS));
                FillEquations.calculateFromApsisSemiMajorAxis(orbit,held(PERIAPSIS));
            } else {
                setAllToZero();
            }
            return;
        }

        if(held(ECCENTRICITY)){
            if(held(SEMI_MAJOR_AXIS)){
                FillEquations.calculateFromEccentricitySemiMajorAxis(orbit);
            } else {
                setAllToZero();
            }
            return;
        }

        if(held(SEMI_MAJOR_AXIS)){
            if(held(VELOCITY_APOAPSIS) || held(VELOCITY_PERIAPSIS)){
                FillEquations.calculateAltitudeFromVelocityAndSMA(orbit,held(VELOCITY_PERIAPSIS));
                FillEquations.calculateFromApsisSemiMajorAxis(orbit,held(VELOCITY_PERIAPSIS));
            } else {
                setAllToZero();
            }
            return;
        }

        setAllToZero();
    }

    private boolean held(KeplerEnums keplerEnums) {
        return orbitalParameterHolds.getHold(keplerEnums);
    }

    private void setAllToZero() {
        for(Map.Entry<KeplerEnums,Kepler> entry : orbit.getKeplarianElements().entrySet()){
            entry.getValue().setData(0.0);
        }
    }

    private int countHolds(OrbitalParameterHolds orbitalParameterHolds) {
        int holds = 0;
        for(Map.Entry<KeplerEnums,Boolean> entry : orbitalParameterHolds.getOrbitalParameterHolds().entrySet()){
            if(entry.getValue()){
                holds++;
            }
        }
        return holds;
    }
}
