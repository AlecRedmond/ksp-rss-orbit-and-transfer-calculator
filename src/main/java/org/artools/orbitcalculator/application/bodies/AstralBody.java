package org.artools.orbitcalculator.application.bodies;

import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;

@Getter
public abstract class AstralBody {
  public static final double G = 6.6743015E-11;
  protected double mass;
  protected double mu;
  @Setter
  protected MotionState motionState;
  protected BodyType sphereOfInfluence;

  protected AstralBody(){}

  public abstract String getName();
  
  public Optional<BodyType> getSphereOfInfluence(){
    return Optional.ofNullable(sphereOfInfluence);
  }
  
  protected double massToMu(){
    return G * mu;
  }

  protected double muToMass(){
    return mu / G;
  }
}
