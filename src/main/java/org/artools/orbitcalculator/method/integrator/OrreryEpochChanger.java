package org.artools.orbitcalculator.method.integrator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.artools.orbitcalculator.application.integrator.OrreryEvent;
import org.artools.orbitcalculator.application.vector.Orrery;

public class OrreryEpochChanger {
  private final Orrery orrery;
  private Instant currentEpoch;
  private List<OrreryEvent> events;
  private OrreryIntegrator integrator;

  public OrreryEpochChanger(Orrery orrery) {
    this.orrery = orrery;
    this.events = new ArrayList<>();
  }

  public void stepToEpoch(Instant newEpoch) {
    if (newEpoch.equals(currentEpoch)) return;
    boolean backwards = newEpoch.isBefore(currentEpoch);
    //TODO - FILL THIS OUT PROPERLY 
  }

  private void sortEvents() {
    this.events =
        events.stream()
            .sorted(Comparator.comparing(OrreryEvent::activationTime))
            .collect(Collectors.toCollection(ArrayList::new));
  }
}
