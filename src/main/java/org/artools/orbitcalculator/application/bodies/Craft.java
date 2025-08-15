package org.artools.orbitcalculator.application.bodies;

import lombok.Data;
import org.artools.orbitcalculator.application.bodies.planets.BodyName;
import org.artools.orbitcalculator.application.vector.MotionState;

import java.util.Optional;

@Data
public class Craft implements AstralBody {
    private MotionState motionState;
    private double mass;
    private BodyName sphereOfInfluence;
    private final String name;


    public Craft(MotionState motionState,double mass,String name){
        this.mass = mass;
        this.motionState = motionState;
        this.name = name;
    }

    @Override
    public double getMu() {
        return mass * G;
    }

    @Override
    public MotionState getMotionState() {
        return motionState;
    }

    @Override
    public Optional<BodyName> getSphereOfInfluence(){
        return Optional.ofNullable(sphereOfInfluence);
    }
}
