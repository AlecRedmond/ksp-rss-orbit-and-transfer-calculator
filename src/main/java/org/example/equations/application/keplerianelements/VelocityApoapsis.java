package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VelocityApoapsis extends Velocity{
    private double data;

    public VelocityApoapsis(double data) {
        this.data = data;
        this.setHold(false);
    }

    @Override
    public String displayName(){
        return "Velocity Ap";
    }
}
