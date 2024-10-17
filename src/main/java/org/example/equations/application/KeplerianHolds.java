package org.example.equations.application;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
public class KeplerianHolds {
    private Map<KeplerEnums, Boolean> keplerianHolds = new HashMap<>();
    
    public KeplerianHolds(){
        buildKeplerianHolds();
    }

    private void buildKeplerianHolds() {
        keplerianHolds.putAll(
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
        keplerianHolds.put(keplerEnum,holdState);
    }
    
    public boolean getHold(KeplerEnums keplerEnum){
        return keplerianHolds.getOrDefault(keplerEnum,false);
    }
}
