package org.artools.orbitcalculator.application.bodies.planets;

public class Earth extends Planet {
    @Override
    protected BodyName planetName() {
        return BodyName.EARTH;
    }

    @Override
    protected String horizonsVectorData() {
    return "X =-2.670489271346819E+07 Y = 1.447801334935187E+08 Z = 7.826551263824105E+03\n"
        + " VX=-2.978545710300538E+01 VY=-5.508905987834387E+00 VZ= 2.317255989376932E-04";
    }

    @Override
    protected double j2() {
        return 0.00108262545;
    }

    @Override
    protected double muHorizons() {
        return 398600.435436;
    }

    @Override
    protected double equatorialRadiusHorizons() {
        return 6378.137;
    }

    @Override
    public BodyName parentBody() {
        return BodyName.SUN;
    }
}
