package org.artools.orbitcalculator.application.bodies.planets;

public class Earth extends Planet {
    @Override
    protected double[] horizonsDataPosition() {
        return new double[]{-2.670576607343695E+07,1.447799719603990E+08,7.826558057785034E+03};
    }

    @Override
    protected double[] horizonsDataVelocity() {
        return new double[]{-2.978542549619797E+01,-5.509082984270721E+00,2.316825644146370E-04};
    }

    @Override
    protected BodyType planetBodyType() {
        return BodyType.EARTH;
    }

    @Override
    public BodyType parentBodyType() {
        return BodyType.SUN;
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
}
