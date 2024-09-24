package org.example.equations.application;

import lombok.Data;
import lombok.Getter;


@Getter
public enum Body {
    EARTH   (6.3714e+6, 3.9860044188e+14);

    private final double radius;
    private final double mu;
    Body(double radius, double mu){
        this.radius = radius;
        this.mu = mu;
    }



}
