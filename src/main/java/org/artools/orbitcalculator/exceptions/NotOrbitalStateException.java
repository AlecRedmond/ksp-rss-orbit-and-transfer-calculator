package org.artools.orbitcalculator.exceptions;

import org.artools.orbitcalculator.application.bodies.AstralBody;

public class NotOrbitalStateException extends ClassCastException {
    public NotOrbitalStateException(AstralBody body){
        super("The motionState for %s could not be cast as an OrbitalState!".formatted(body));
    }
}
