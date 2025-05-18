package org.example.equations.method.vector;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;
import org.example.equations.application.vector.OrbitalVectors;
import org.example.equations.application.vector.ReferenceFrame;

import java.util.Optional;

import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.FRAME_TRANSFORM;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.VECTOR_OPERATOR;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationOrder.ZXZ;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

public class AngleTransform {
    private static final double TOLERANCE = 1e-6;
    private static final Vector3D X_AXIS = Vector3D.PLUS_I;
    private static final Vector3D Y_AXIS = Vector3D.PLUS_J;
    private static final Vector3D Z_AXIS = Vector3D.PLUS_K;
    private static final Plane XY_PLANE = new Plane(Z_AXIS, TOLERANCE);

    public OrbitalVectors rotateCraftVectors(OrbitalVectors orbitalVectors, ReferenceFrame finalFrame) {
        var rotation =
                getRotationTransform(
                        orbitalVectors.getFrame(),
                        finalFrame,
                        orbitalVectors.getOrbit(),
                        orbitalVectors.getTrueAnomaly());
        return rotation.map(value -> rotateAll(orbitalVectors, value, finalFrame)).orElse(orbitalVectors);
    }

    public Optional<Vector3D> transformToFrame(Vector3D vector, ReferenceFrame initialFrame, OrbitalVectors orbitalVectors) {
        var rotation = getRotationTransform(initialFrame, orbitalVectors.getFrame(), orbitalVectors.getOrbit(), orbitalVectors.getTrueAnomaly());
        return rotation.map(rot -> rot.applyTo(vector));
    }

    private Optional<Rotation> getRotationTransform(
            ReferenceFrame initialFrame, ReferenceFrame finalFrame, Orbit orbit, double trueAnomaly) {

        Rotation rotation = null;
        switch (finalFrame) {
            case CRAFT -> rotation = rotateToCraftFrame(initialFrame, orbit, trueAnomaly);
            case PLANAR -> rotation = rotateToPlanarFrame(initialFrame, orbit, trueAnomaly);
            case INERTIAL -> rotation = rotateToInertialFrame(initialFrame, orbit, trueAnomaly);
        }

        return Optional.ofNullable(rotation);
    }

    private OrbitalVectors rotateAll(
            OrbitalVectors orbitalVectors, Rotation rotation, ReferenceFrame finalFrame) {
        var velocity = orbitalVectors.getVelocity();
        var position = orbitalVectors.getPosition();
        var momentum = orbitalVectors.getMomentum();
        orbitalVectors.setVelocity(rotation.applyTo(velocity));
        orbitalVectors.setPosition(rotation.applyTo(position));
        orbitalVectors.setMomentum(rotation.applyTo(momentum));
        orbitalVectors.setFrame(finalFrame);
        return orbitalVectors;
    }

    private Rotation rotateToCraftFrame(
            ReferenceFrame initialFrame, Orbit orbit, double trueAnomaly) {
        switch (initialFrame) {
            case CRAFT -> {
                return null;
            }
            case PLANAR -> {
                return rotateByTrueAnomaly(-trueAnomaly);
            }
            case INERTIAL -> {
                return rotateByTrueAnomaly(-trueAnomaly)
                        .compose(rotateInertialToPlanar(orbit, false), VECTOR_OPERATOR);
            }
        }
        return null;
    }

    private Rotation rotateToPlanarFrame(
            ReferenceFrame initialFrame, Orbit orbit, double trueAnomaly) {
        switch (initialFrame) {
            case CRAFT -> {
                return rotateByTrueAnomaly(trueAnomaly);
            }
            case PLANAR -> {
                return null;
            }
            case INERTIAL -> {
                return rotateInertialToPlanar(orbit, false);
            }
        }
        return null;
    }

    private Rotation rotateToInertialFrame(
            ReferenceFrame initialFrame, Orbit orbit, double trueAnomaly) {
        switch (initialFrame) {
            case CRAFT -> {
                return rotateInertialToPlanar(orbit, true)
                        .compose(rotateByTrueAnomaly(trueAnomaly), VECTOR_OPERATOR);
            }
            case PLANAR -> {
                return rotateInertialToPlanar(orbit, true);
            }
            case INERTIAL -> {
                return null;
            }
        }
        return null;
    }

    private Rotation rotateByTrueAnomaly(double trueAnomaly) {
        return new Rotation(Z_AXIS, trueAnomaly, VECTOR_OPERATOR);
    }

    private Rotation rotateInertialToPlanar(Orbit orbit, boolean reversed) {

        var rightAscension = orbit.getDataFor(RIGHT_ASCENSION);
        var inclination = orbit.getDataFor(INCLINATION);
        var argumentPE = orbit.getDataFor(ARGUMENT_PE);

        return reversed
                ? new Rotation(ZXZ, FRAME_TRANSFORM, -argumentPE, -inclination, -rightAscension)
                : new Rotation(ZXZ, FRAME_TRANSFORM, rightAscension, inclination, argumentPE);
    }

    protected Vector3D toInertialFrame(
            Vector3D vector, double rightAscension, double inclination, double argumentPE) {
        Rotation rotation =
                new Rotation(ZXZ, FRAME_TRANSFORM, -argumentPE, -inclination, -rightAscension);
        return rotation.applyTo(vector);
    }

    /**
     * Returns the line at which two orbits in the same frame intersect each other. Will return the line in inertial frame.
     */
    public Optional<Line> intersect(Orbit orbitA, Orbit orbitB) {
        Plane planeA = orbitalPlane(orbitA);
        Plane planeB = orbitalPlane(orbitB);
        return Optional.ofNullable(planeA.intersection(planeB));
    }

    protected Plane orbitalPlane(Orbit orbit) {
        return orbitalPlane(
                orbit.getDataFor(RIGHT_ASCENSION),
                orbit.getDataFor(INCLINATION),
                orbit.getDataFor(ARGUMENT_PE));
    }

    protected Plane orbitalPlane(double rightAscension, double inclination, double argumentPE) {
        Vector3D normalVector = toInertialFrame(Z_AXIS, rightAscension, inclination, argumentPE);
        return new Plane(normalVector, TOLERANCE);
    }
}
