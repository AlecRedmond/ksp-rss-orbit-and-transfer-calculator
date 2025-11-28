package org.artools.orbitcalculator.exceptions;

public class OrbitStateNotFoundException extends RuntimeException {
    public OrbitStateNotFoundException(String id) {
        super("Orbit state not found with ID: " + id);
    }
}
