package org.artools.orbitcalculator.application.integrator;

import java.time.Instant;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.vector.MotionState;

@Data
public class CraftEngineBurn implements OrreryEvent {
  private final String id;
  private final CraftItinerary itinerary;
  private Vector3D thrustDirection;
  private Instant burnStart;
  private Instant burnEnd;
  private MotionState burnStartState;
  private MotionState burnEndState;
  private double initialMass;
  private double finalMass;
  private double initialDeltaV;
  private double finalDeltaV;
  private double expendedDeltaV;

  public CraftEngineBurn(String id, CraftItinerary itinerary) {
    this.id = id;
    this.itinerary = itinerary;
  }

  public Craft getCraft() {
    return itinerary.getCraft();
  }

  @Override
  public Instant getInitializationTime() {
    return burnStart;
  }

  @Override
  public Instant deactivationTime() {
    return burnEnd;
  }
}
