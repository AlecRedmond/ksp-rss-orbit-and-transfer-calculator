package org.artools.orbitcalculator.exceptions;

import org.artools.orbitcalculator.application.bodies.AstralBodies;

public class NotOrbitalStateException extends ClassCastException {
    public NotOrbitalStateException(AstralBodies astralBodies){
        super("The motionState for " + astralBodies + " could not be cast as an OrbitalState!");
    }
}
