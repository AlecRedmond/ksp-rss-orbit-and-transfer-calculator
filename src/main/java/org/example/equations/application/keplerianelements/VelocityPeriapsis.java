package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VelocityPeriapsis extends Velocity {
    private double data;

    public VelocityPeriapsis(double data) {
        this.data = data;
        this.setHold(false);
    }

    public Double get() {
        return this.data;
    }

    public void set(double data){
        this.data = data;
    }

    @Override
    public String displayName(){
        return "Velocity Pe";
    }
}
