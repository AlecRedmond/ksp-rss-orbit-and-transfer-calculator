package org.artools.orbitcalculator.application.bodies.astralbodies;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SunTest {
    Sun test = new Sun();

    @Test
    void motionState(){
        var motionState = test.getMotionState1951Jan1();
        assertNotNull(motionState);
    }
}
