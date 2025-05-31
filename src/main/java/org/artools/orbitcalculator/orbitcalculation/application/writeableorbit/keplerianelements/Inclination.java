package org.artools.orbitcalculator.orbitcalculation.application.writeableorbit.keplerianelements;

import lombok.Data;

@Data
public class Inclination implements Kepler {

    private double data;

    public Inclination(double data) {
        this.data = data;
    }

    @Override
    public KeplerEnums getType() {
        return KeplerEnums.INCLINATION;
    }

    @Override
    public String getAsString() {
        double degrees = Math.toDegrees(data);
        return String.format("%.2f",degrees);
    }

    @Override
    public void setFromString(String string) {
        double degrees = Double.parseDouble(string);
        data = Math.toRadians(degrees);
    }

    @Override
    public String displayName() {
        return "Inclination (°)";
    }

    @Override
    public String unitSI() {
        return "°";
    }

    @Override
    public double getData() {
        return data;
    }

    @Override
    public void setData(double data) {
        this.data = data;
    }

}
