package org.artools.orbitcalculator.exceptions;

import org.artools.orbitcalculator.application.bodies.Body;

public class NotOrbitalStateException extends ClassCastException {
    public NotOrbitalStateException(Body body){
        super("The motionState for " + body + " could not be cast as an OrbitalState!");
    }
}
