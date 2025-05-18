package org.example.equations.method.vector;

import org.example.equations.application.Orbit;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VelocityChangeControllerTest {
    VelocityChangeController test;

    @Test
    void intersectionAnomaly() {
        test = new VelocityChangeController();
        Orbit orbitA = new OrbitBuilder(250000,250000).getOrbit();
        Orbit orbitB = new OrbitBuilder(250000,1000000,90,90,0).getOrbit();
        var anomaly = test.intersectionAnomaly(orbitA,orbitB);
        assertTrue(anomaly.isPresent());
    }

    @Test
    void bestInclinationChange() {
        test = new VelocityChangeController();
        Orbit orbitA = new OrbitBuilder(250000,250000).getOrbit();
        Orbit orbitB = new OrbitBuilder(250000,1000000,90,90,0).getOrbit();
        var bestChange = test.bestInclinationChange(orbitA,orbitB).getVelocityChange();
        System.out.println(bestChange);
      }
}