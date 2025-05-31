package org.artools.orbitcalculator.equations.application.keplerianelements;

import lombok.Data;

@Data
public abstract class KeplerBase implements Kepler{

    protected double data;

    @Override
    public double getData() {
        return 0;
    }

    @Override
    public void setData(double data) {
        this.data = data;
    }
}
