package org.artools.orbitcalculator.equations.application;

import lombok.Data;
import org.artools.orbitcalculator.equations.application.keplerianelements.OrbitalPeriod;

@Data
public class Intercept {
    private double interceptTime;
    private double satelliteLeadTime;
    private String satelliteLeadTimeString;
    private String interceptTimeString;
    private boolean isAscending;

    public Intercept(double interceptTime, boolean isAscending, double satelliteLeadTime){
        this.interceptTime = interceptTime;
        this.satelliteLeadTime = satelliteLeadTime;
        this.isAscending = isAscending;
        OrbitalPeriod orbitalPeriod = new OrbitalPeriod(interceptTime);
        interceptTimeString = orbitalPeriod.getAsString();
        orbitalPeriod.setData(satelliteLeadTime);
        satelliteLeadTimeString = orbitalPeriod.getAsString();
    }
}
