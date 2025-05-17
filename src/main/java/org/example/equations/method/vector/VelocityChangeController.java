package org.example.equations.method.vector;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.ReferenceFrame;
import org.example.equations.application.vector.VelocityChange;

@NoArgsConstructor
@Getter
public class VelocityChangeController {
    private VelocityChange velocityChange;

    public VelocityChangeController inclinationChange(Orbit initialOrbit, Orbit finalOrbit) {

        return this;
    }

    private void calculateDetlaV(){
        ReferenceFrame frame = velocityChange.getInitialVectors().getFrame();
        if(!velocityChange.getFinalVectors().getFrame().equals(frame)){

        }
    }

    private void zeroVelocityChange() {
        velocityChange = null;
    }
}
