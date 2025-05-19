package org.example.equations.application.vector;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;
import org.example.equations.application.Orbit;

@Data
@Builder
public class OrbitalVectors {
    private Body centralBody;
    private Vector3D position;
    private Vector3D velocity;
    private Vector3D momentum;
    private Vector3D eccentricity;
    private double rightAscension;
    private double inclination;
    private double argumentPE;
    private double trueAnomaly;
    private double eccentricAnomaly;
    private double meanAnomaly;
}
