package org.artools.orbitcalculator.application.bodies.planets;

public class Mars extends Planet {
    @Override
    protected BodyType planetBodyType() {
        return BodyType.MARS;
    }

    @Override
    protected double[] horizonsDataPosition() {
        return new double[]{1.897101050733607E+08,-8.148545875716785E+07,-6.400388342317320E+06};
    }

    @Override
    protected double[] horizonsDataVelocity() {
        return new double[]{1.050422117548903E+01,2.431602730943637E+01,2.499037440832712E-01};
    }

    @Override
    protected double j2() {
        return 0;
    }

    @Override
    protected double muHorizons() {
        return 42828.375662;
    }

    @Override
    protected double equatorialRadiusHorizons() {
        return 3396.19;
    }

    @Override
    public BodyType parentBodyType() {
        return BodyType.SUN;
    }
}
