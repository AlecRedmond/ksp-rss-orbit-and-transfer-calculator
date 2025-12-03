package org.artools.orbitcalculator.application.integrator;

import java.time.Duration;
import java.time.Instant;

public interface OrreryEvent {
  default boolean isActive(Instant epoch) {
    if (epoch.equals(activationTime())) {
      return true;
    }
    return epoch.isAfter(activationTime()) && epoch.isBefore(deactivationTime());
  }

  Instant activationTime();

  Instant deactivationTime();

  default Duration timeUntilStateChange(Instant epoch) {
    Duration duration = Duration.between(epoch, activationTime());
    if (duration.isPositive()) return duration;
    return Duration.between(epoch, deactivationTime());
  }
}
