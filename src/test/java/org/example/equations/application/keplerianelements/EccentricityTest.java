package org.example.equations.application.keplerianelements;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class EccentricityTest {
    static double testData = 0.02;
    static String testString = "0.020";
    static Eccentricity data;

    @BeforeAll
    static void generateApoapsis(){
    data = new Eccentricity();
    }

    @Test
    void set() {
        data.set(testData);
        assertEquals(testData, data.get());
    }

    @Test
    void getAsString() {
        data.set(testData);
        String test = data.getAsString();
        assertEquals(testString,test);
    }

    @Test
    void setFromString() {
        data.setFromString(testString);
        assertEquals(testData, data.get());
    }
}