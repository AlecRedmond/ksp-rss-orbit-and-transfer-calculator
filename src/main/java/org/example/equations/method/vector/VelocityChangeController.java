package org.example.equations.method.vector;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.ReferenceFrame;
import org.example.equations.application.vector.VelocityChange;

import java.util.NoSuchElementException;
import java.util.Optional;

@NoArgsConstructor
@Getter
public class VelocityChangeController {
    private VelocityChange velocityChange;

    public Optional<Double> intersectionAnomaly(Orbit referenceOrbit, Orbit intersectingOrbit) {
        try {
            var transform = new AngleTransform();
            var vectorController = new OrbitalVectorController();
            var intersectLine = transform.intersect(referenceOrbit, intersectingOrbit).get();
            var referencePeriapseVectors = vectorController.buildVectors(referenceOrbit, 0).changeFrame(ReferenceFrame.CRAFT).getVectors();
            var directionVector = transform.transformToFrame(intersectLine.getDirection(), ReferenceFrame.INERTIAL, referencePeriapseVectors).get();
            var firstAnomaly = directionVector.getY() >= 0 ? Vector3D.angle(referencePeriapseVectors.getPosition(), directionVector) : Math.PI - Vector3D.angle(referencePeriapseVectors.getPosition(), directionVector);
            return Optional.of(firstAnomaly);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private void calculateDetlaV() {
        ReferenceFrame frame = velocityChange.getInitialVectors().getFrame();
        if (!velocityChange.getFinalVectors().getFrame().equals(frame)) {

        }
    }

    private void zeroVelocityChange() {
        velocityChange = null;
    }
}
