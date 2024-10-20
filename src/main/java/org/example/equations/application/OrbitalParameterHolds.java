package org.example.equations.application;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
@AllArgsConstructor
public class OrbitalParameterHolds {
    private Map<KeplerEnums, Boolean> orbitalParameterHolds = new HashMap<>();
    
    public OrbitalParameterHolds(){
        buildHolds();
    }

    private void buildHolds() {
        orbitalParameterHolds.putAll(
                Map.of(
                        APOAPSIS, false,
                        PERIAPSIS, false,
                        ECCENTRICITY, false,
                        SEMI_MAJOR_AXIS, false,
                        ORBITAL_PERIOD, false,
                        VELOCITY_APOAPSIS, false,
                        VELOCITY_PERIAPSIS, false));
    }
    
    public void setHold(KeplerEnums keplerEnum, boolean holdState){
        orbitalParameterHolds.put(keplerEnum,holdState);
    }
    
    public boolean getHold(KeplerEnums keplerEnum){
        return orbitalParameterHolds.getOrDefault(keplerEnum,false);
    }
}
