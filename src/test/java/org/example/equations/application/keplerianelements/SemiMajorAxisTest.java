package org.example.equations.application.keplerianelements;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SemiMajorAxisTest {
    static double testData = 350e3;
    static String testString = "350.00 km";
    static SemiMajorAxis data;

    @BeforeAll
    static void generateApoapsis(){
        data = new SemiMajorAxis();
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