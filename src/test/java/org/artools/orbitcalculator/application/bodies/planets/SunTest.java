package org.artools.orbitcalculator.application.bodies.planets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SunTest {
    Sun test = new Sun();

    @Test
    void motionState(){
        var motionState = test.getMotionState();
        assertNotNull(motionState);
    }
}
