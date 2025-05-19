package org.example.equations.method.vector;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.CraftVectors;
import org.example.equations.application.vector.OrbitalVectors;
import org.example.equations.application.vector.ReferenceFrame;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.ECCENTRICITY;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.SEMI_MAJOR_AXIS;
import static org.example.equations.application.vector.ReferenceFrame.CRAFT;
import static org.example.equations.application.vector.ReferenceFrame.INERTIAL;

@Getter
@NoArgsConstructor
public class OrbitalVectorController {
    private OrbitalVectors vectors;

    public OrbitalVectorController buildVectors(CraftVectors vectors) {

        return this;
    }


}
