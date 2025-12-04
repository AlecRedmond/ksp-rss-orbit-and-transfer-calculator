package org.artools.orbitcalculator.exceptions;

import org.artools.orbitcalculator.application.kepler.KeplerOrbit;

public class UnBuiltOrbitException extends IllegalStateException {
  public UnBuiltOrbitException(KeplerOrbit orbit) {
    super("Attempted to use an orbit which was not built. id: %s".formatted(orbit.getId()));
  }
}
