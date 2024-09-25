package org.example.equations.application;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Keplerian {
    private double eccentricity, semiMajorAxis;
    private double inclination, longitudeOfAscendingNode;
    private double argumentOfPeriapsis, trueAnomaly;
    private Body body = Body.EARTH;
    private double apoapsis, periapsis;

    public void setAllToZero(){
        this.eccentricity = 0;
        this.semiMajorAxis = 0;
        this.apoapsis = 0;
        this.periapsis = 0;
        this.inclination = 0;
        this.longitudeOfAscendingNode = 0;
        this.argumentOfPeriapsis = 0;
        this.trueAnomaly = 0;
    }

}
