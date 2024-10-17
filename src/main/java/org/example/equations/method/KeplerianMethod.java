package org.example.equations.method;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.equations.application.Keplerian;
import org.example.equations.application.KeplerianHolds;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

import java.util.Map;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

@Data
@NoArgsConstructor
public class KeplerianMethod {
    private Keplerian keplerian;
    private KeplerianHolds keplerianHolds;

    public KeplerianMethod(Keplerian keplerian,KeplerianHolds keplerianHolds){
        this.keplerian = keplerian;
        this.keplerianHolds = keplerianHolds;
        methodFromHolds();
    }

    private void methodFromHolds() {
        int holdsPressed = countHolds(keplerianHolds);
        if(holdsPressed != 2){
            setAllToZero();
            return;
        }
        if(held(ORBITAL_PERIOD)){
            FillEquations.convertOrbitalPeriodToSMA(keplerian);
            keplerianHolds.setHold(SEMI_MAJOR_AXIS,true);
        }

        if(held(APOAPSIS) || held(PERIAPSIS)){
            if(held(APOAPSIS) && held(PERIAPSIS)){
                FillEquations.calculateFromPeriapsisApoapsis(keplerian);
            } else if(held(ECCENTRICITY)){
                FillEquations.calculateFromApsisEccentricity(keplerian,held(PERIAPSIS));
            } else if(held(SEMI_MAJOR_AXIS)){
                FillEquations.calculateFromApsisSemiMajorAxis(keplerian,held(PERIAPSIS));
            } else if(held(VELOCITY_PERIAPSIS) && held(PERIAPSIS) || held(VELOCITY_APOAPSIS) && held(APOAPSIS)){
                FillEquations.calculateSMAFromVelocityAndAltitude(keplerian,held(PERIAPSIS));
                FillEquations.calculateFromApsisSemiMajorAxis(keplerian,held(PERIAPSIS));
            } else {
                setAllToZero();
            }
            return;
        }

        if(held(ECCENTRICITY)){
            if(held(SEMI_MAJOR_AXIS)){
                FillEquations.calculateFromEccentricitySemiMajorAxis(keplerian);
            } else {
                setAllToZero();
            }
            return;
        }

        if(held(SEMI_MAJOR_AXIS)){
            if(held(VELOCITY_APOAPSIS) || held(VELOCITY_PERIAPSIS)){
                FillEquations.calculateAltitudeFromVelocityAndSMA(keplerian,held(VELOCITY_PERIAPSIS));
                FillEquations.calculateFromApsisSemiMajorAxis(keplerian,held(VELOCITY_PERIAPSIS));
            } else {
                setAllToZero();
            }
            return;
        }

        setAllToZero();
    }

    private boolean held(KeplerEnums keplerEnums) {
        return keplerianHolds.getHold(keplerEnums);
    }

    private void setAllToZero() {
        for(Map.Entry<KeplerEnums,Kepler> entry : keplerian.getKeplarianElements().entrySet()){
            entry.getValue().setData(0.0);
        }
    }

    private int countHolds(KeplerianHolds keplerianHolds) {
        int holds = 0;
        for(Map.Entry<KeplerEnums,Boolean> entry : keplerianHolds.getKeplerianHolds().entrySet()){
            if(entry.getValue()){
                holds++;
            }
        }
        return holds;
    }
}
