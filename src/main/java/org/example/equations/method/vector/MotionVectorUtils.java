package org.example.equations.method.vector;

import java.util.Optional;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.vector.MotionVectors;

public class MotionVectorUtils {

  public Optional<MotionVectors> changeCentralBody(
      MotionVectors motionVectors, MotionVectors centralBodyVectors) {
    if (!motionVectors.getEpoch().equals(centralBodyVectors.getEpoch())) {
      return Optional.empty();
    }
    if (motionVectors.getFrame().equals(MotionVectors.Frame.VELOCITY_FRAME)) {
      motionVectors = changeToInertial(motionVectors);
    }
    if (centralBodyVectors.getFrame().equals(MotionVectors.Frame.VELOCITY_FRAME)) {
      centralBodyVectors = changeToInertial(centralBodyVectors);
    }

    var centralBody = centralBodyVectors.getCentralBody();
    var newVelocity = motionVectors.getVelocity().add(centralBodyVectors.getVelocity());
    var newRadius = motionVectors.getRadius().add(centralBodyVectors.getRadius());
    var newRotation = combineRotations(motionVectors, centralBodyVectors);
    var epoch = centralBodyVectors.getEpoch();
    return Optional.of(
        new MotionVectors(
            centralBody,
            newVelocity,
            newRadius,
            newRotation,
            epoch,
            MotionVectors.Frame.BODY_INERTIAL_FRAME));
  }

  public MotionVectors changeToInertial(MotionVectors motionVectors) {
    var rotate = motionVectors.getRotationToInertial();
    return new MotionVectors(
        motionVectors.getCentralBody(),
        rotate.applyTo(motionVectors.getVelocity()),
        rotate.applyTo(motionVectors.getRadius()),
        motionVectors.getRotationToInertial(),
        motionVectors.getEpoch(),
        MotionVectors.Frame.BODY_INERTIAL_FRAME);
  }

  private Rotation combineRotations(MotionVectors motionVectors, MotionVectors centralBodyVectors) {
    var motionRotation = Optional.ofNullable(motionVectors.getRotationToInertial());
    var centralRotation = Optional.ofNullable(centralBodyVectors.getRotationToInertial());
    if (centralRotation.isPresent() && motionRotation.isPresent()) {
      return motionRotation
          .get()
          .compose(centralRotation.get(), RotationConvention.FRAME_TRANSFORM);
    } else
      return centralRotation.orElseGet(
          () ->
              motionRotation.orElse(
                  new Rotation(Vector3D.PLUS_K, 0, RotationConvention.FRAME_TRANSFORM)));
  }
}
