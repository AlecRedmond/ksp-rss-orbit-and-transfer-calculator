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
    private Body body;
    private double apoapsis, periapsis;



}
