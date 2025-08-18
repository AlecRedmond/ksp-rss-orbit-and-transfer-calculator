package org.artools.orbitcalculator.application.bodies.planets;

public class Saturn extends Planet {
    @Override
    protected BodyType planetBodyType() {
        return BodyType.SATURN;
    }

    @Override
    protected double[] horizonsDataPosition() {
        return new double[]{-1.412383751516021E+09,7.367255944998446E+07,5.480876239785470E+07};
    }

    @Override
    protected double[] horizonsDataVelocity() {
        return new double[]{-1.019555997058242E+00,-9.668944347402187E+00,2.103533639655706E-01};
    }

    @Override
    protected double j2() {
        return 0;
    }

    @Override
    protected double muHorizons() {
        return 37931206.234;
    }

    @Override
    protected double equatorialRadiusHorizons() {
        return 60268;
    }

    @Override
    public BodyType parentBodyType() {
        return BodyType.SUN;
    }
}
