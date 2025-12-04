package org.artools.orbitcalculator.method.integrator;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.artools.orbitcalculator.application.integrator.CraftEngineBurn;
import org.artools.orbitcalculator.application.integrator.CraftItinerary;
import org.artools.orbitcalculator.application.integrator.OrreryEvent;
import org.artools.orbitcalculator.application.vector.Orrery;

public class OrreryEpochController {
  @Getter private final Orrery orrery;
  private final OrreryIntegrator integrator;
  private final List<OrreryEvent> events;

  public OrreryEpochController(Orrery orrery) {
    this.orrery = orrery;
    this.events = new ArrayList<>();
    this.integrator = new OrreryIntegrator(orrery);
  }

  public void addEvent(OrreryEvent event) {
    Instant eventStart = event.getInitializationTime();
    if (!eventStart.isAfter(currentEpoch())) {
      stepToEpoch(eventStart);
    }
    events.add(event);
    if (eventStart.equals(currentEpoch())) {
      switchOrreryEvent(true);
    }
  }

  public void removeEvent(OrreryEvent event) {
    Instant eventStart = event.getInitializationTime();
    if (!eventStart.isAfter(currentEpoch())) {
      stepToEpoch(eventStart);
    }
    events.remove(event);
    if (eventStart.equals(currentEpoch())) {
      switchOrreryEvent(true);
    }
  }

  public void stepToEpoch(Instant newEpoch) {
    if (newEpoch.equals(currentEpoch())) return;
    boolean forwards = !newEpoch.isBefore(currentEpoch());
    Duration durationToStep = getDurationToStep(newEpoch, forwards);
    integrator.stepByDuration(durationToStep);
    if (newEpoch.equals(currentEpoch())) return;
    switchOrreryEvent(forwards);
    stepToEpoch(newEpoch);
  }

  private Instant currentEpoch() {
    return orrery.getEpoch();
  }

  private void switchOrreryEvent(boolean forwards) {
    events.stream()
        .filter(event -> event.getInitializationTime().equals(currentEpoch()))
        .forEach(e -> switchEvents(e, forwards, forwards));

    events.stream()
        .filter(event -> event.deactivationTime().equals(currentEpoch()))
        .forEach(e -> switchEvents(e, !forwards, forwards));
  }

  private void switchEvents(OrreryEvent event, boolean activate, boolean forwards) {
    switch (event) {
      case CraftEngineBurn ceb -> {
        if (activate) integrator.addCraftBurn(ceb, forwards);
        else integrator.removeCraftBurn(ceb, forwards);
      }
      case CraftItinerary ci -> {
        if (activate) integrator.addCraft(ci, forwards);
        else integrator.removeCraft(ci, forwards);
      }
      default -> throw new IllegalStateException("Unexpected value: " + event);
    }
  }

  private Duration getDurationToStep(Instant newEpoch, boolean forwards) {
    Duration durationToStep = Duration.between(currentEpoch(), newEpoch);
    Optional<Duration> toNextEventOpt = durationUntilNextEvent(newEpoch, forwards);
    if (toNextEventOpt.isEmpty()) {
      return durationToStep;
    }
    Duration toNextEvent = toNextEventOpt.get();
    if (durationToStep.abs().compareTo(toNextEvent.abs()) > 0) {
      return toNextEvent;
    }
    return durationToStep;
  }

  private Optional<Duration> durationUntilNextEvent(Instant newEpoch, boolean forwards) {
    return events.stream()
        .map(event -> event.timeUntilStateChange(newEpoch))
        .filter(duration -> duration.isPositive() == forwards)
        .min(Comparator.comparing(Duration::abs));
  }
}
