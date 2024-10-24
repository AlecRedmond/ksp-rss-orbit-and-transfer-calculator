package org.example.equations.method;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RendezvousTest {

    @Test
    public void testRendezvous(){
        double orbitTime = 3600 * 1.4069;
        Rendezvous rendezvous = new Rendezvous(orbitTime);
        ArrayList<Integer> myInts = rendezvous.getIntercepts();
        System.out.println(myInts);
    }
}
