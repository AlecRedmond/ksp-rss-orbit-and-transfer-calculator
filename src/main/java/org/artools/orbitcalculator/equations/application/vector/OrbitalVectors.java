package org.artools.equations.application.vector;

import java.time.Instant;

import lombok.*;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.equations.application.Body;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrbitalVectors extends MotionVectors {
    private Body centralBody;
    private Vector3D position;
    private Vector3D velocity;
    private Vector3D momentum;
    private Vector3D eccentricity;
    private double semiMajorAxis;
    private double rightAscension;
    private double inclination;
    private double argumentPE;
    private double trueAnomaly;
    private double eccentricAnomaly;
    private double meanAnomaly;
    private Instant epoch;
}
