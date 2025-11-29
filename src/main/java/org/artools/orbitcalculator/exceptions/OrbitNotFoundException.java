package org.artools.orbitcalculator.exceptions;

public class OrbitNotFoundException extends RuntimeException {
    public OrbitNotFoundException(String id) {
        super("Orbit state not found with ID: " + id);
    }
}
