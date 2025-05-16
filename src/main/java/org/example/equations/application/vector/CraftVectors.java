package org.example.equations.application.vector;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Data
@Builder
public class CraftVectors {
    private Vector3D position;
    private Vector3D velocity;
    private Vector3D momentum;
    private ReferenceFrame frame;
}
