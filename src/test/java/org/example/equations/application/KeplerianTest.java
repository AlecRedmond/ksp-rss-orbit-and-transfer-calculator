package org.example.equations.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeplerianTest {
    Keplerian keplerian = new Keplerian();

    @Test
    public void doesItEqualNull(){
        assertEquals(null,keplerian.getApoapsis());
    }
}
