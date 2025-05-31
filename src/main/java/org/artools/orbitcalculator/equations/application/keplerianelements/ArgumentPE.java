package org.artools.equations.application.keplerianelements;

import lombok.Data;

@Data
public class ArgumentPE implements Kepler{
    private double data;

    public ArgumentPE(double data) {
        this.data = data;
    }

    @Override
    public Kepler.KeplerEnums getType() {
        return Kepler.KeplerEnums.ARGUMENT_PE;
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
        return "Arg. of Periapsis (°)";
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
