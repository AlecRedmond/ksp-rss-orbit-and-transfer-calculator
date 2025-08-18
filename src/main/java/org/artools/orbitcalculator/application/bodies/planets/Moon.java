package org.artools.orbitcalculator.application.bodies.planets;

public class Moon extends Planet {
    @Override
    protected BodyType planetBodyType() {
        return BodyType.MOON;
    }

    @Override
    protected double[] horizonsDataPosition() {
        return new double[]{-2.708072660629534E+07,1.447290072549995E+08,-9.409324759691954E+02};
    }

    @Override
    protected double[] horizonsDataVelocity() {
        return new double[]{-2.958836421559424E+01,-6.520860805106235E+00,-8.638598259733810E-02};
    }

    @Override
    protected double j2() {
        return 0;
    }

    @Override
    protected double muHorizons() {
        return 4902.800066;
    }

    @Override
    protected double equatorialRadiusHorizons() {
        return 1737.53;
    }

    @Override
    public BodyType parentBodyType() {
        return BodyType.EARTH;
    }
}
