package org.artools.orbitcalculator.application.bodies.planets;

public class Moon extends Planet {
    @Override
    protected BodyType planetBodyType() {
        return BodyType.MOON;
    }

    @Override
    protected String horizonsVectorData() {
    return "X =-2.707985902329372E+07 Y = 1.447291984553363E+08 Z =-9.383994639739394E+02\n"
        + " VX=-2.958847807556421E+01 VY=-6.520695053001154E+00 VZ=-8.638786284631017E-02";
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
