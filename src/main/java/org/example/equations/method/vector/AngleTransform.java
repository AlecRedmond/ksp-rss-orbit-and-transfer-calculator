package org.example.equations.method.vector;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.example.equations.application.Orbit;

import java.util.Optional;

import static org.apache.commons.math3.geometry.euclidean.threed.RotationConvention.VECTOR_OPERATOR;
import static org.apache.commons.math3.geometry.euclidean.threed.RotationOrder.ZXZ;
import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

public class AngleTransform {
    private static final double TOLERANCE = 1e-6;
    private static final Vector3D X_AXIS = Vector3D.PLUS_I;
    private static final Vector3D Y_AXIS = Vector3D.PLUS_J;
    private static final Vector3D Z_AXIS = Vector3D.PLUS_K;
    private static final Plane XY_PLANE = new Plane(Z_AXIS, TOLERANCE);

    public Rotation getInertialFromCraft(double rightAscension, double inclination, double argumentPE, double trueAnomaly){
        Rotation craftToPlanar = getPlanarFromCraft(trueAnomaly);
        Rotation planarToInertial = getInertialFromPlanar(rightAscension, inclination, argumentPE);
        return planarToInertial.compose(craftToPlanar,VECTOR_OPERATOR);
    }

    private static Rotation getPlanarFromCraft(double trueAnomaly) {
        return new Rotation(Z_AXIS, -trueAnomaly, VECTOR_OPERATOR);
    }

    private static Rotation getInertialFromPlanar(double rightAscension, double inclination, double argumentPE) {
        return new Rotation(ZXZ, VECTOR_OPERATOR, -argumentPE, -inclination, -rightAscension);
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
        Rotation planarToInertial = getInertialFromPlanar(rightAscension,inclination,argumentPE);
        Vector3D normalVector = planarToInertial.applyTo(Z_AXIS);
        return new Plane(normalVector, TOLERANCE);
    }
}
