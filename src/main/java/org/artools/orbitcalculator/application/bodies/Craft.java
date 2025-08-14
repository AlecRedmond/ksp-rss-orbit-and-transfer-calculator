package org.artools.orbitcalculator.application.bodies;

import lombok.Data;
import org.artools.orbitcalculator.application.vector.MotionState;

@Data
public class Craft implements AstralBody {
    private MotionState motionState;
    private double mass;
    private BodyType bodyType;


    public Craft(MotionState motionState,double mass){
        this.mass = mass;
        this.motionState = motionState;
        this.bodyType = BodyType.CRAFT;
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
    public BodyType getBodyType() {
        return bodyType;
    }
}
