package org.artools.orbitcalculator.application.bodies.planets;

public class Saturn extends Planet {
    @Override
    protected BodyName planetName() {
        return BodyName.SATURN;
    }

    @Override
    protected String horizonsVectorData() {
    return "X =-1.412383751516021E+09 Y = 7.367255944998446E+07 Z = 5.480876239785470E+07\n"
        + " VX=-1.019555997058242E+00 VY=-9.668944347402187E+00 VZ= 2.103533639655706E-01";
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
    public BodyName parentBody() {
        return BodyName.SUN;
    }
}
