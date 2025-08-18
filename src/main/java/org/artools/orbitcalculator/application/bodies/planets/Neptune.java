package org.artools.orbitcalculator.application.bodies.planets;

public class Neptune extends Planet {
    @Override
    protected BodyType planetBodyType() {
        return BodyType.NEPTUNE;
    }

    @Override
    protected double[] horizonsDataPosition() {
        return new double[]{-4.302388271496875E+09,-1.420277015028722E+09,1.283614498353369E+08};
    }

    @Override
    protected double[] horizonsDataVelocity() {
        return new double[]{1.673256606721457E+00,-5.127288187325322E+00,6.702792483632902E-02};
    }

    @Override
    protected double j2() {
        return 0;
    }

    @Override
    protected double muHorizons() {
        return 6835099.97;
    }

    @Override
    protected double equatorialRadiusHorizons() {
        return 24766;
    }

    @Override
    public BodyType parentBodyType() {
        return BodyType.SUN;
    }
}
