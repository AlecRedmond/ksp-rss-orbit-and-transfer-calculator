package org.example.equations.application.keplerianelements;

import lombok.Data;

@Data
public abstract class KeplerBase implements Kepler{

    protected double data;

    @Override
    public KeplarianElement getType() {
        return null;
    }

    @Override
    public String getAsString() {
        return null;
    }

    @Override
    public void setFromString(String string) {

    }

    @Override
    public String displayName() {
        return null;
    }

    @Override
    public String unitSI() {
        return null;
    }

    @Override
    public double getData() {
        return 0;
    }

    @Override
    public void setData(double data) {
        this.data = data;
    }
}
