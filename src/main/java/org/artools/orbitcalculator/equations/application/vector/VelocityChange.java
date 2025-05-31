package org.artools.orbitcalculator.equations.application.vector;

import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Data
public class VelocityChange {
    private OrbitalVectors initialVectors;
    private OrbitalVectors finalVectors;
    private Vector3D deltaV;
}
