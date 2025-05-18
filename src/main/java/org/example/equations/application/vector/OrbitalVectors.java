package org.example.equations.application.vector;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;

@Data
@Builder
public class OrbitalVectors {
    private Vector3D position;
    private Vector3D velocity;
    private Vector3D momentum;
    private Vector3D eccentricity;
    private Vector3D ascendingNode;
    private double trueAnomaly;
    private Orbit orbit;
}
