package org.artools.orbitcalculator.application.bodies.astralbodies;

public class Neptune extends AstralBody{
    @Override
    protected String horizonsVectorData() {
    return "X =-4.302388320559609E+09 Y =-1.420276864687885E+09 Z = 1.283614478699602E+08\n"
        + " VX= 1.673256343417634E+00 VY=-5.127288024535035E+00 VZ= 6.702817080825230E-02";
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
}
