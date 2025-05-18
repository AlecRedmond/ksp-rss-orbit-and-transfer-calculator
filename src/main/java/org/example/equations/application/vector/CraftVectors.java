package org.example.equations.application.vector;

import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Body;

@Data
public class CraftVectors {
    private Vector3D position;
    private Vector3D velocity;
    private Body focalBody;
}
