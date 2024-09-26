package org.example.equations.application.keplerianelements;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VelocityPeriapsisTest {
    static double testData = 3000;
    static String testString = "3,000 m/s";
    static VelocityPeriapsis velocityPeriapsis;
    static Velocity velocity;

    @BeforeAll
    public static void startPeri(){
        velocityPeriapsis = new VelocityPeriapsis();
        velocity = new Velocity();
    }

    @Test
    public void testSetter(){
        velocity.set(2000.0);
        double newData1 = velocity.get();
        assertEquals(2000,newData1);
        velocityPeriapsis.set(testData);
        double newData = velocityPeriapsis.get();
        assertEquals(3000,newData);
        assertNotEquals(velocity.get(),velocityPeriapsis.get());
    }


}
