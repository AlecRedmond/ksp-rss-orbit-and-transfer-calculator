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
        velocity.setData(2000.0);
        double newData1 = velocity.getData();
        assertEquals(2000,newData1);
        velocityPeriapsis.setData(testData);
        double newData = velocityPeriapsis.getData();
        assertEquals(3000,newData);
        assertNotEquals(velocity.getData(),velocityPeriapsis.getData());
    }


}
