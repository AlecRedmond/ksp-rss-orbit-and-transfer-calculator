package org.artools.orbitcalculator.orbitcalculation.application.vector;

import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Data
public class VelocityChange {
    private OrbitalState initialVectors;
    private OrbitalState finalVectors;
    private Vector3D deltaV;
}
