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

    @Override
    public String displayName(){
        return "Velocity Pe";
    }
}
