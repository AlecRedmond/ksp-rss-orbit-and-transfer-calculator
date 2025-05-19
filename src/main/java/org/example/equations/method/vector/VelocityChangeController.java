package org.example.equations.method.vector;

import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.OrbitalVectors;
import org.example.equations.application.vector.ReferenceFrame;
import org.example.equations.application.vector.VelocityChange;

@NoArgsConstructor
@Getter
public class VelocityChangeController {
  private VelocityChange velocityChange;

  public VelocityChangeController bestInclinationChange(Orbit initialOrbit, Orbit referenceOrbit) {
    var firstAnomalyOptional = intersectionAnomaly(initialOrbit, referenceOrbit);
    if (firstAnomalyOptional.isEmpty()) {
      zeroVelocityChange();
      return this;
    }
    double firstAnomaly = firstAnomalyOptional.get();
    double secondAnomaly = firstAnomaly + Math.PI;
    OrbitalVectorController vectorController = new OrbitalVectorController();
    var firstVectors =
        vectorController
            .buildVectors(initialOrbit, firstAnomaly)
            .changeFrame(ReferenceFrame.INERTIAL)
            .getVectors();
    var secondVectors =
        vectorController
            .buildVectors(initialOrbit, secondAnomaly)
            .changeFrame(ReferenceFrame.INERTIAL)
            .getVectors();

    var bestVectors =
        firstVectors.getVelocity().getNorm() <= secondVectors.getVelocity().getNorm()
            ? firstVectors
            : secondVectors;

    double bestAnomaly = bestVectors.equals(firstVectors) ? firstAnomaly : secondAnomaly;

    var velocity = bestVectors.getVelocity();
    var referenceFrameVectors =
        vectorController
            .buildVectors(referenceOrbit, 0)
            .changeFrame(ReferenceFrame.CRAFT)
            .getVectors();
    var finalVelocity = new AngleTransform().transformToFrame(velocity,ReferenceFrame.INERTIAL,referenceFrameVectors);


    buildVelocityChange(bestVectors, newInclinedVectors);

    return this;
  }

  private void buildVelocityChange(OrbitalVectors initialVectors, OrbitalVectors finalVectors) {
    if (!initialVectors.getFrame().equals(ReferenceFrame.INERTIAL)) {
      initialVectors =
          new AngleTransform().craftToInertial(initialVectors, ReferenceFrame.INERTIAL);
    }
    if (!finalVectors.getFrame().equals(ReferenceFrame.INERTIAL)) {
      finalVectors = new AngleTransform().craftToInertial(finalVectors, ReferenceFrame.INERTIAL);
    }
    if (!positionsEqual(initialVectors, finalVectors)) {
      return;
    }
    velocityChange.setInitialVectors(initialVectors);
    velocityChange.setInitialVectors(finalVectors);
    var velocityInitial = initialVectors.getVelocity();
    var velocityFinal = finalVectors.getVelocity();
    var deltaVelocityInertial = velocityFinal.subtract(velocityInitial);

    initialVectors = new AngleTransform().craftToInertial(initialVectors, ReferenceFrame.CRAFT);

    var deltaVelocityCraft =
        new AngleTransform()
            .transformToFrame(deltaVelocityInertial, ReferenceFrame.INERTIAL, initialVectors);
    deltaVelocityCraft.ifPresent(vector3D -> velocityChange.setDeltaV(vector3D));
  }

  private boolean positionsEqual(OrbitalVectors vectorsA, OrbitalVectors vectorsB) {
    var positionA = vectorsA.getPosition();
    var positionB = vectorsB.getPosition();
    var distance = Vector3D.distance(positionA, positionB);
    return Math.abs(distance) < 1e-3;
  }

  protected Optional<Double> intersectionAnomaly(Orbit referenceOrbit, Orbit intersectingOrbit) {
    var transform = new AngleTransform();
    var vectorController = new OrbitalVectorController();
    var intersectLineOptional = transform.intersect(referenceOrbit, intersectingOrbit);
    if (intersectLineOptional.isEmpty()) {
      return Optional.empty();
    }
    var intersectLine = intersectLineOptional.get();
    var referencePeriapseVectors =
        vectorController
            .buildVectors(referenceOrbit, 0)
            .changeFrame(ReferenceFrame.CRAFT)
            .getVectors();
    var directionVectorOptional =
        transform.transformToFrame(
            intersectLine.getDirection(), ReferenceFrame.INERTIAL, referencePeriapseVectors);
    if (directionVectorOptional.isEmpty()) {
      return Optional.empty();
    }
    var directionVector = directionVectorOptional.get();
    var firstAnomaly =
        directionVector.getY() >= 0
            ? Vector3D.angle(referencePeriapseVectors.getPosition(), directionVector)
            : Math.PI - Vector3D.angle(referencePeriapseVectors.getPosition(), directionVector);
    return Optional.of(firstAnomaly);
  }

  private void calculateDetlaV() {
    ReferenceFrame frame = velocityChange.getInitialVectors().getFrame();
    if (!velocityChange.getFinalVectors().getFrame().equals(frame)) {}
  }

  private void zeroVelocityChange() {
    velocityChange = null;
  }
}
