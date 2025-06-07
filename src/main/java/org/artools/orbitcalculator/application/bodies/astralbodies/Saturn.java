package org.artools.orbitcalculator.application.bodies.astralbodies;

import org.artools.orbitcalculator.application.bodies.AstralBodies;

public class Saturn extends AstralBody{
    @Override
    protected String horizonsVectorData() {
    return "X =-1.412383721620868E+09 Y = 7.367284295993097E+07 Z = 5.480875622993410E+07\n"
        + " VX=-1.019558097108260E+00 VY=-9.668944201961397E+00 VZ= 2.103534313781799E-01";
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
    public AstralBodies getDefaultOrbitalFocus() {
        return AstralBodies.SUN;
    }
}
