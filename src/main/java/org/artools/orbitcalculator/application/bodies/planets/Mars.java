package org.artools.orbitcalculator.application.bodies.planets;

import org.artools.orbitcalculator.application.bodies.BodyType;

public class Mars extends Planet {
    @Override
    protected BodyType planetName() {
        return BodyType.MARS;
    }

    @Override
    protected String horizonsVectorData() {
    return "X = 1.897097970704435E+08 Y =-8.148617174405721E+07 Z =-6.400395669880252E+06\n"
        + " VX= 1.050430483104508E+01 VY= 2.431599133118027E+01 VZ= 2.499009259251110E-01";
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
    public BodyType parentBody() {
        return BodyType.SUN;
    }
}
