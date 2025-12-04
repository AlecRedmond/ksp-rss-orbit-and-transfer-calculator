package org.artools.orbitcalculator.application.integrator;

import java.time.Duration;
import java.time.Instant;

public interface OrreryEvent {
  default boolean isActive(Instant epoch) {
    if (epoch.equals(getInitializationTime())) {
      return true;
    }
    return epoch.isAfter(getInitializationTime()) && epoch.isBefore(deactivationTime());
  }

  Instant getInitializationTime();

  Instant deactivationTime();

  default Duration timeUntilStateChange(Instant epoch) {
    Duration duration = Duration.between(epoch, getInitializationTime());
    if (duration.isPositive()) return duration;
    return Duration.between(epoch, deactivationTime());
  }
}
